/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/gendustry
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/gendustry/master/MMPL-1.0.txt
 */

package net.bdew.gendustry.power

import ic2.api.item.{ElectricItem, ISpecialElectricItem}
import net.minecraft.item.ItemStack
import net.bdew.gendustry.config.Tuning
import net.minecraft.entity.EntityLivingBase
import net.bdew.gendustry.compat.PowerProxy
import cpw.mods.fml.common.Optional
import net.bdew.lib.power.ItemPoweredBase

@Optional.Interface(modid = PowerProxy.IC2_MOD_ID, iface = "ic2.api.energy.tile.ISpecialElectricItem")
trait ItemPoweredEU extends ItemPoweredBase with ISpecialElectricItem {
  private lazy val ratio = Tuning.getSection("Power").getFloat("EU_MJ_Ratio")
  private lazy val manager = new ItemPoweredEUManager(this)

  override def useCharge(stack: ItemStack, uses: Int, player: EntityLivingBase) = {
    if (PowerProxy.haveIC2)
      ElectricItem.rawManager.chargeFromArmor(stack, player)
    super.useCharge(stack, uses, player)
  }

  def canProvideEnergy(itemStack: ItemStack) = false
  def getChargedItemId(itemStack: ItemStack) = itemID
  def getEmptyItemId(itemStack: ItemStack) = itemID
  def getMaxCharge(itemStack: ItemStack) = (maxCharge * ratio).round
  def getTier(itemStack: ItemStack) = 2
  def getTransferLimit(itemStack: ItemStack) = 2048
  def getManager(itemStack: ItemStack) = manager
}

