/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/gendustry
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/gendustry/master/MMPL-1.0.txt
 */

package net.bdew.gendustry.machines.extractor

import net.bdew.gendustry.Gendustry
import net.minecraft.entity.player.EntityPlayer
import net.bdew.lib.gui.{Texture, Color, Rect, BaseScreen}
import net.bdew.lib.gui.widgets.{WidgetLabel, WidgetFluidGauge}
import net.bdew.lib.Misc
import net.bdew.gendustry.gui.{HintIcons, WidgetPowerCustom, WidgetProgressBarNEI, Textures}

class GuiExtractor(val te: TileExtractor, player: EntityPlayer) extends BaseScreen(new ContainerExtractor(te, player), 176, 166) {
  val background = Texture(Gendustry.modId, "textures/gui/extractor.png", rect)

  override def initGui() {
    super.initGui()
    widgets.add(new WidgetProgressBarNEI(new Rect(79, 41, 53, 15), Textures.greenProgress(53), te.progress, "Extractor"))
    widgets.add(new WidgetPowerCustom(new Rect(8, 19, 16, 58), Textures.powerFill, te.power))
    widgets.add(new WidgetFluidGauge(new Rect(152, 19, 16, 58), Textures.tankOverlay, te.tank))
    widgets.add(new WidgetLabel(Misc.toLocal("tile.gendustry.extractor.name"), 8, 6, Color.darkgray))

    inventorySlots.getSlotFromInventory(te, te.slots.inIndividual).setBackgroundIcon(HintIcons.droneOrSapling)
    inventorySlots.getSlotFromInventory(te, te.slots.inLabware).setBackgroundIcon(HintIcons.labware)
  }
}