/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/gendustry
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/gendustry/master/MMPL-1.0.txt
 */

package net.bdew.gendustry.items

import net.bdew.lib.items.{ItemUtils, SimpleItem}
import net.bdew.lib.covers.{TileCoverable, ItemCover}
import net.minecraftforge.common.ForgeDirection
import net.minecraft.inventory.{IInventory, ISidedInventory}
import net.minecraft.client.renderer.texture.IconRegister
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.gendustry.Gendustry
import net.bdew.lib.Misc
import net.minecraft.item.ItemStack

class ImportCover(id: Int) extends SimpleItem(id, "ImportCover") with ItemCover {
  override def getCoverIcon(stack: ItemStack) = itemIcon
  override def getSpriteNumber = 0

  override def isValidTile(te: TileCoverable, stack: ItemStack) = te.isInstanceOf[ISidedInventory with IInventory]

  override def tickCover(te: TileCoverable, side: ForgeDirection, coverStack: ItemStack): Unit = {
    if (te.worldObj.getTotalWorldTime % 20 != 0) return
    val inv = te.asInstanceOf[ISidedInventory with IInventory]
    val insertSlots = inv.getAccessibleSlotsFromSide(side.ordinal())
    for {
      from <- Misc.getNeighbourTile(te, side, classOf[IInventory])
      slot <- ItemUtils.getAccessibleSlotsFromSide(from, side.getOpposite)
      stack <- Option(from.getStackInSlot(slot))
      if (Misc.asInstanceOpt(from, classOf[ISidedInventory]) map (_.canExtractItem(slot, stack, side.getOpposite.ordinal()))
        getOrElse true)
    } {
      from.setInventorySlotContents(slot, ItemUtils.addStackToSlots(stack, inv, insertSlots, true))
      from.onInventoryChanged()
    }
  }

  @SideOnly(Side.CLIENT)
  override def registerIcons(reg: IconRegister) {
    itemIcon = reg.registerIcon(Gendustry.modId + ":covers/import")
  }
}
