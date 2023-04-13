package qouteall.imm_ptl.peripheral.dim_stack;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import qouteall.imm_ptl.core.CHelper;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.peripheral.alternate_dimension.AlternateDimensions;
import qouteall.q_misc_util.my_util.GuiHelper;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class DimStackScreen extends Screen {
    @org.jetbrains.annotations.Nullable
    public final Screen parent;
    private final Button finishButton;
    private final Button toggleButton;
    private final Button addDimensionButton;
    private final Button removeDimensionButton;
    private final Button editButton;
    
    private final Button helpButton;
    
    private final Button loopButton;
    private final Button gravityModeButton;

    private final Button saveAsGlobalConfig;
    
    private int titleY;
    
    public boolean isEnabled = false;
    public final DimListWidget dimListWidget;
    
    public boolean loopEnabled = false;
    public boolean localGravityEnabled = false;
    
    public final Function<Screen,List<ResourceKey<Level>>> dimensionListSupplier;
    private final Consumer<DimStackInfo> finishCallback;
    
    public DimStackScreen(
        @Nullable Screen parent,
        Function<Screen,List<ResourceKey<Level>>> dimensionListSupplier,
        Consumer<DimStackInfo> finishCallback
    ) {
        super(new TranslatableComponent("imm_ptl.altius_screen"));
        this.parent = parent;
        this.dimensionListSupplier = dimensionListSupplier;
        this.finishCallback = finishCallback;
        
        toggleButton = new Button(
            0, 0, 150, 20,
            new TranslatableComponent("imm_ptl.altius_toggle_false"),
            (buttonWidget) -> {
                setEnabled(!isEnabled);
            }
        );
        
        loopButton = new Button(
            0, 0, 150, 20,
            new TranslatableComponent("imm_ptl.loop_disabled"),
            (buttonWidget) -> {
                loopEnabled = !loopEnabled;
                buttonWidget.setMessage(new TranslatableComponent(
                    loopEnabled ? "imm_ptl.loop_enabled" : "imm_ptl.loop_disabled"
                ));
            }
        );
        
        gravityModeButton = new Button(
            0, 0, 150, 20,
            new TranslatableComponent("imm_ptl.dim_stack.gravity_transform_disabled"),
            (buttonWidget) -> {
                localGravityEnabled = !localGravityEnabled;
                buttonWidget.setMessage(new TranslatableComponent(
                    localGravityEnabled ? "imm_ptl.dim_stack.gravity_transform_enabled" :
                        "imm_ptl.dim_stack.gravity_transform_disabled"
                ));
            }
        );
        
        finishButton = new Button(
            0, 0, 72, 20,
            new TranslatableComponent("imm_ptl.finish"),
            (buttonWidget) -> {
                Minecraft.getInstance().setScreen(parent);
                finishCallback.accept(getDimStackInfo());
            }
        );
        addDimensionButton = new Button(
            0, 0, 72, 20,
            new TranslatableComponent("imm_ptl.dim_stack_add"),
            (buttonWidget) -> {
                onAddEntry();
            }
        );
        removeDimensionButton = new Button(
            0, 0, 72, 20,
            new TranslatableComponent("imm_ptl.dim_stack_remove"),
            (buttonWidget) -> {
                onRemoveEntry();
            }
        );
        
        editButton = new Button(
            0, 0, 72, 20,
            new TranslatableComponent("imm_ptl.dim_stack_edit"),
            (buttonWidget) -> {
                onEditEntry();
            }
        );

        saveAsGlobalConfig = new Button(
                0, 0, 72, 20,
                new TextComponent("Save to disk"),
                (buttonWidget) -> {
                    onSaveDimStack();
                }
        );
        
        dimListWidget = new DimListWidget(
            width,
            height,
            100,
            200,
            DimEntryWidget.widgetHeight,
            this,
            DimListWidget.Type.mainDimensionList
        );
        
        Consumer<DimEntryWidget> callback = getElementSelectCallback();
        if (IPGlobal.enableAlternateDimensions) {
            dimListWidget.entryWidgets.add(createDimEntryWidget(AlternateDimensions.alternate5));
            dimListWidget.entryWidgets.add(createDimEntryWidget(AlternateDimensions.alternate1));
        }
        dimListWidget.entryWidgets.add(createDimEntryWidget(Level.OVERWORLD));
        dimListWidget.entryWidgets.add(createDimEntryWidget(Level.NETHER));
        
        helpButton = createHelpButton(this);

        if (Paths.get(FMLPaths.CONFIGDIR.get().toString(), "imm_ptl_dim_stack.json").toFile().exists()) {
            try {
                String dimStackConfig = Files.readString(Paths.get(FMLPaths.CONFIGDIR.get().toString(), "imm_ptl_dim_stack.json"));
                JsonElement JSON = new GsonBuilder().create().fromJson(dimStackConfig, JsonElement.class);
                DataResult<CompoundTag> result = CompoundTag.CODEC.parse(JsonOps.INSTANCE, JSON);
                DimStackInfo info = DimStackInfo.fromNbt(result.result().get());
                isEnabled = true;
                loopEnabled = info.loop;
                localGravityEnabled = info.gravityChange;
                dimListWidget.entryWidgets.retainAll(new ArrayList<DimEntryWidget>());
                info.entries.stream().forEach(dimStackEntry -> {
                    DimEntryWidget widget =
                            new DimEntryWidget(dimStackEntry.dimension, dimListWidget, getElementSelectCallback(), DimEntryWidget.Type.withAdvancedOptions, dimStackEntry);
                    dimListWidget.entryWidgets.add(widget);
                });
            } catch (JsonSyntaxException | IOException e) {
                LogManager.getLogger().error("Failed to read ImmersivePortals Dimension Stack Config: ", e);
            }
        }
    }

    private void onSaveDimStack() {
        if (getDimStackInfo() == null) {
            try {
                Files.deleteIfExists(Paths.get(FMLPaths.CONFIGDIR.get().toString(), "imm_ptl_dim_stack.json"));
            } catch (IOException e) {
                LogManager.getLogger().error("Failed to delete ImmersivePortals Dimension Stack Config: ", e);
            }
        } else {
            try (FileWriter writer = new FileWriter(Paths.get(FMLPaths.CONFIGDIR.get().toString(), "imm_ptl_dim_stack.json").toFile())) {
                DataResult<JsonElement> result = CompoundTag.CODEC.encodeStart(JsonOps.INSTANCE, getDimStackInfo().toNbt());
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(result.result().get()));
            } catch (IOException e) {
                LogManager.getLogger().error("Failed to write ImmersivePortals Dimension Stack Config: ", e);
            }
        }
    }

    public static Button createHelpButton(Screen parent) {
        return new Button(
            0, 0, 30, 20,
            new TextComponent("?"),
            button -> {
                CHelper.openLinkConfirmScreen(
                    parent, "https://qouteall.fun/immptl/wiki/Dimension-Stack"
                );
            }
        );
    }
    
    private DimEntryWidget createDimEntryWidget(ResourceKey<Level> dimension) {
        return new DimEntryWidget(dimension, dimListWidget, getElementSelectCallback(), DimEntryWidget.Type.withAdvancedOptions);
    }
    
    @Nullable
    public DimStackInfo getDimStackInfo() {
        if (isEnabled) {
            return new DimStackInfo(
                dimListWidget.entryWidgets.stream().map(
                    dimEntryWidget -> dimEntryWidget.entry
                ).collect(Collectors.toList()),
                loopEnabled,
                localGravityEnabled
            );
        }
        else {
            return null;
        }
    }
    
    @Override
    protected void init() {
        
        addRenderableWidget(toggleButton);
        addRenderableWidget(finishButton);
        addRenderableWidget(addDimensionButton);
        addRenderableWidget(removeDimensionButton);
        
        addRenderableWidget(editButton);
        addRenderableWidget(helpButton);
        addRenderableWidget(loopButton);
        addRenderableWidget(gravityModeButton);

        if (IPGlobal.editGlobalDimensionStack) {
            addRenderableWidget(saveAsGlobalConfig);
        }
        
        setEnabled(isEnabled);
        
        addWidget(dimListWidget);
        
        dimListWidget.update();
        
        GuiHelper.layout(
            0, height,
            GuiHelper.blankSpace(5),
            new GuiHelper.LayoutElement(true, 20, (from, to) -> {
                helpButton.x = width - 50;
                helpButton.y = from;
                helpButton.setWidth(30);
            }),
            new GuiHelper.LayoutElement(true, 20, (from, to) -> {
                saveAsGlobalConfig.x = width - 150;
                saveAsGlobalConfig.y = from - 20;
                saveAsGlobalConfig.setWidth(90);
            }),
            new GuiHelper.LayoutElement(
                true, 20,
                GuiHelper.combine(
                    GuiHelper.layoutButtonVertically(toggleButton),
                    GuiHelper.layoutButtonVertically(loopButton),
                    GuiHelper.layoutButtonVertically(gravityModeButton)
                )
            ),
            GuiHelper.blankSpace(5),
            new GuiHelper.LayoutElement(false, 1, (from, to) -> {
                dimListWidget.updateSize(
                    width, height,
                    from, to
                );
            }),
            GuiHelper.blankSpace(5),
            new GuiHelper.LayoutElement(true, 20, (from, to) -> {
                finishButton.y = from;
                addDimensionButton.y = from;
                removeDimensionButton.y = from;
                editButton.y = from;
                GuiHelper.layout(
                    0, width,
                    GuiHelper.blankSpace(10),
                    new GuiHelper.LayoutElement(
                        false, 1,
                        GuiHelper.layoutButtonHorizontally(finishButton)
                    ),
                    GuiHelper.blankSpace(5),
                    new GuiHelper.LayoutElement(
                        false, 1,
                        GuiHelper.layoutButtonHorizontally(addDimensionButton)
                    ),
                    GuiHelper.blankSpace(5),
                    new GuiHelper.LayoutElement(
                        false, 1,
                        GuiHelper.layoutButtonHorizontally(removeDimensionButton)
                    ),
                    GuiHelper.blankSpace(5),
                    new GuiHelper.LayoutElement(
                        false, 1,
                        GuiHelper.layoutButtonHorizontally(editButton)
                    ),
                    GuiHelper.blankSpace(10)
                );
            }),
            GuiHelper.blankSpace(5)
        );
        
        GuiHelper.layout(
            0, width,
            GuiHelper.blankSpace(10),
            new GuiHelper.LayoutElement(
                false, 10, GuiHelper.layoutButtonHorizontally(toggleButton)
            ),
            GuiHelper.blankSpace(5),
            new GuiHelper.LayoutElement(
                false, 8, GuiHelper.layoutButtonHorizontally(loopButton)
            ),
            GuiHelper.blankSpace(5),
            new GuiHelper.LayoutElement(
                false, 10, GuiHelper.layoutButtonHorizontally(gravityModeButton)
            ),
            GuiHelper.blankSpace(10)
        );
    }
    
    @Override
    public void onClose() {
        // When `esc` is pressed return to the parent screen rather than setting screen to `null` which returns to the main menu.
        this.minecraft.setScreen(this.parent);
    }
    
    private Consumer<DimEntryWidget> getElementSelectCallback() {
        return w -> dimListWidget.setSelected(w);
    }
    
    @Override
    public void render(PoseStack matrixStack, int mouseY, int i, float f) {
        this.renderBackground(matrixStack);
        
        
        if (isEnabled) {
            dimListWidget.render(matrixStack, mouseY, i, f);
        }
        
        super.render(matrixStack, mouseY, i, f);
        
        Font textRenderer = Minecraft.getInstance().font;
        textRenderer.drawShadow(
            matrixStack, this.title,
            20, 10, -1
        );
        
    }
    
    private void setEnabled(boolean cond) {
        isEnabled = cond;
        if (isEnabled) {
            toggleButton.setMessage(new TranslatableComponent("imm_ptl.altius_toggle_true"));
        }
        else {
            toggleButton.setMessage(new TranslatableComponent("imm_ptl.altius_toggle_false"));
        }
        addDimensionButton.visible = isEnabled;
        removeDimensionButton.visible = isEnabled;
        editButton.visible = isEnabled;
        loopButton.visible = isEnabled;
        gravityModeButton.visible = isEnabled;
    }
    
    private void onAddEntry() {
        DimEntryWidget selected = dimListWidget.getSelected();
        
        int position;
        if (selected == null) {
            position = 0;
        }
        else {
            position = dimListWidget.entryWidgets.indexOf(selected);
        }
        
        if (position < 0 || position > dimListWidget.entryWidgets.size()) {
            position = -1;
        }
        
        int insertingPosition = position + 1;
        
        Minecraft.getInstance().setScreen(
            new SelectDimensionScreen(
                this,
                dimensionType -> {
                    dimListWidget.entryWidgets.add(
                        insertingPosition,
                        createDimEntryWidget(dimensionType)
                    );
                    removeDuplicate(insertingPosition);
                    dimListWidget.update();
                }
            )
        );

//        IPGlobal.preTotalRenderTaskList.addTask(MyTaskList.withDelay(1, () -> {
//
//            return true;
//        }));
    }
    
    private void onRemoveEntry() {
        DimEntryWidget selected = dimListWidget.getSelected();
        if (selected == null) {
            return;
        }
        
        int position = dimListWidget.entryWidgets.indexOf(selected);
        
        if (position == -1) {
            return;
        }
        
        dimListWidget.entryWidgets.remove(position);
        dimListWidget.update();
    }
    
    private void onEditEntry() {
        DimEntryWidget selected = dimListWidget.getSelected();
        if (selected == null) {
            return;
        }
        
        Minecraft.getInstance().setScreen(new DimStackEntryEditScreen(
            this, selected
        ));
    }
    
    private void removeDuplicate(int insertedIndex) {
        ResourceKey<Level> inserted = dimListWidget.entryWidgets.get(insertedIndex).dimension;
        for (int i = dimListWidget.entryWidgets.size() - 1; i >= 0; i--) {
            if (dimListWidget.entryWidgets.get(i).dimension == inserted) {
                if (i != insertedIndex) {
                    dimListWidget.entryWidgets.remove(i);
                }
            }
        }
    }
    
}
