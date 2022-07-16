package qouteall.imm_ptl.peripheral.guide;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.core.McHelper;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.q_misc_util.my_util.MyTaskList;
import qouteall.q_misc_util.my_util.SignalArged;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class IPGuide {

    public static final SignalArged<Portal> clientPortalSpawnSignal = new SignalArged<>();

    public static class GuideInfo {
        public boolean wikiInformed = false;
        public boolean portalHelperInformed = false;
        public boolean lagInformed = false;
        
        public GuideInfo() {}
    }
    
    private static GuideInfo readFromFile() {
        File storageFile = getStorageFile();
        
        if (storageFile.exists()) {
            
            GuideInfo result = null;
            try (FileReader fileReader = new FileReader(storageFile)) {
                result = IPGlobal.gson.fromJson(fileReader, GuideInfo.class);
            }
            catch (Throwable e) {
                e.printStackTrace();
                return new GuideInfo();
            }
            
            if (result == null) {
                return new GuideInfo();
            }
            
            return result;
        }
        
        return new GuideInfo();
    }
    
    private static File getStorageFile() {
        return new File(Minecraft.getInstance().gameDirectory, "imm_ptl_state.json");
    }
    
    private static void writeToFile(GuideInfo guideInfo) {
        try (FileWriter fileWriter = new FileWriter(getStorageFile())) {
            
            IPGlobal.gson.toJson(guideInfo, fileWriter);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static GuideInfo guideInfo = new GuideInfo();
    
    public static void initClient() {
        guideInfo = readFromFile();
        
        clientPortalSpawnSignal.connect(p -> {
            LocalPlayer player = Minecraft.getInstance().player;
            
            if (!guideInfo.wikiInformed) {
                if (player != null && player.isCreative()) {
                    guideInfo.wikiInformed = true;
                    writeToFile(guideInfo);
                    informWithURL(
                        "https://qouteall.fun/immptl/wiki/Portal-Customization",
                        new TranslatableComponent("imm_ptl.inform_wiki")
                    );
                }
            }
            
            if (!guideInfo.lagInformed) {
                if (player != null) {
                    guideInfo.lagInformed = true;
                    writeToFile(guideInfo);
                    
                    IPGlobal.clientTaskList.addTask(MyTaskList.withDelay(100, () -> {
                        Minecraft.getInstance().gui.handleChat(
                            ChatType.SYSTEM,
                            new TranslatableComponent("imm_ptl.about_lag"),
                            Util.NIL_UUID
                        );
                        return true;
                    }));
                }
            }
        });
    }
    
    public static void onClientPlacePortalHelper() {
        if (!guideInfo.portalHelperInformed) {
            guideInfo.portalHelperInformed = true;
            writeToFile(guideInfo);
            
            informWithURL(
                "https://qouteall.fun/immptl/wiki/Portal-Customization#portal-helper-block",
                new TranslatableComponent("imm_ptl.inform_portal_helper")
            );
        }
    }
    
    private static void informWithURL(String link, MutableComponent text) {
        Minecraft.getInstance().gui.handleChat(
            ChatType.SYSTEM,
            text.append(
                McHelper.getLinkText(link)
            ),
            Util.NIL_UUID
        );
    }

    @OnlyIn(Dist.CLIENT)
    public static class RemoteCallables {
        public static void showWiki() {
            informWithURL(
                "https://qouteall.fun/immptl/wiki/Commands-Reference",
                new TextComponent("")
            );
        }
    }
}
