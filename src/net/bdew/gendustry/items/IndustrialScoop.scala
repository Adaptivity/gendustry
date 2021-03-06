/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/gendustry
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/gendustry/master/MMPL-1.0.txt
 */

package net.bdew.gendustry.items

import net.minecraft.item.{ItemStack, EnumToolMaterial, ItemTool}
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import net.bdew.gendustry.Gendustry
import net.bdew.gendustry.config.Tuning
import net.bdew.lib.Misc
import net.minecraft.block.Block
import net.minecraft.entity.EntityLivingBase
import java.util
import net.minecraft.creativetab.CreativeTabs
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IconRegister
import cpw.mods.fml.common.Optional
import net.bdew.gendustry.compat.PowerProxy
import net.bdew.gendustry.power.ItemPowered
import forestry.api.core.IToolScoop
import net.minecraftforge.common.{ForgeHooks, MinecraftForge}

@Optional.Interface(modid = PowerProxy.IC2_MOD_ID, iface = "ic2.api.item.ISpecialElectricItem")
class IndustrialScoop(id: Int) extends ItemTool(id, 0, EnumToolMaterial.IRON, Array.empty[Block]) with ItemPowered with IToolScoop {
  lazy val cfg = Tuning.getSection("Items").getSection("IndustrialScoop")
  lazy val mjPerCharge = cfg.getInt("MjPerCharge")
  lazy val maxCharge = cfg.getInt("Charges") * mjPerCharge

  setUnlocalizedName(Gendustry.modId + ".scoop")
  setMaxStackSize(1)
  setMaxDamage(101)

  MinecraftForge.setToolClass(this, "scoop", 3)

  efficiencyOnProperMaterial = 32

  override def getStrVsBlock(stack: ItemStack, block: Block) = getStrVsBlock(stack, block, 1)
  override def getStrVsBlock(stack: ItemStack, block: Block, meta: Int) =
    if (!hasCharges(stack))
      0.1F
    else if (ForgeHooks.isToolEffective(stack, block, meta))
      efficiencyOnProperMaterial
    else
      0.1F

  override def onBlockDestroyed(stack: ItemStack, world: World, blockId: Int, x: Int, y: Int, z: Int, player: EntityLivingBase): Boolean = {
    if (ForgeHooks.isToolEffective(stack, Block.blocksList(world.getBlockId(x, y, z)), world.getBlockMetadata(x, y, z))) {
      useCharge(stack, 1, player)
      return true
    }
    return false
  }

  override def hitEntity(stack: ItemStack, target: EntityLivingBase, player: EntityLivingBase): Boolean = false

  override def addInformation(stack: ItemStack, player: EntityPlayer, l: util.List[_], par4: Boolean) = {
    import scala.collection.JavaConverters._
    val tip = l.asInstanceOf[util.List[String]].asScala

    tip += Misc.toLocalF("gendustry.label.charges", getCharge(stack) / mjPerCharge)
  }

  override def getSubItems(par1: Int, tabs: CreativeTabs, l: util.List[_]) {
    import scala.collection.JavaConverters._
    val items = l.asInstanceOf[util.List[ItemStack]].asScala
    items += new ItemStack(this)
    items += stackWithCharge(maxCharge)
  }

  override def getItemEnchantability: Int = 0
  override def getIsRepairable(par1ItemStack: ItemStack, par2ItemStack: ItemStack): Boolean = false
  override def isBookEnchantable(itemstack1: ItemStack, itemstack2: ItemStack): Boolean = false

  @SideOnly(Side.CLIENT)
  override def registerIcons(reg: IconRegister) {
    itemIcon = reg.registerIcon(Gendustry.modId + ":scoop")
  }
}
