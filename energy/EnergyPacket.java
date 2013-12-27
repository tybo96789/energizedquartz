package tybo96789.energizedquartz.energy;
/*
 * THIS CLASS WILL HANDLE THE CURRENT ENERGY IT WILL SERVE AS THE CONNECTION TO GET SOURCE AND RECEPTOR TO THE TILEENEITY
 */

import java.util.ArrayList;

import net.minecraft.block.Block;
import tybo96789.energizedquartz.tiles.TileEnergy;

public class EnergyPacket {
	
	private TileEnergy source, receptor, currentLoc, controller;
	
	private Block blk;
	
	private double energy;
	
	public EnergyPacket(TileEnergy src, TileEnergy des)
	{
		this.source = src;
		this.receptor = des;
		this.source.packet(this.getEnergyPacket());
		this.receptor.packet(this.getEnergyPacket());
		this.blk = src.getBlockType(); //TODO Check possible server crash usage
	}

	public TileEnergy getSource() {
		return this.source;
	}

	public void setSource(TileEnergy source) {
		this.source = source;
	}

	public TileEnergy getReceptor() {
		return this.receptor;
	}

	public void setReceptor(TileEnergy receptor) {
		this.receptor = receptor;
	}
	
	public void updateLoc(TileEnergy tile)
	{
		this.currentLoc = tile;
	}
	
	public Block getBlock()
	{
		return this.blk;
	}
	
	public double getEnergy()
	{
		return this.energy;
	}
	public void setEnergy(double amt)
	{
		this.energy = amt;
	}
	
	//TODO Check Usage
	@Deprecated
	public boolean transferEnergy(ArrayList<TileEnergy> net)
	{
		return true;
	}
	public EnergyPacket getEnergyPacket()
	{
		return this;
	}
	
	

}
