package tybo96789.energizedquartz.tiles;

/*
 * TILEVOLTAGE SERVES AS THE FOUNDATION OF HOW ENERGY IS MOVED THOUGHTOUT THE ENERGY NET, AND ALSO IS USED TO MAKE CONNECTIONS FOR SOURCES AND RECEPTORS
 */

import java.util.ArrayList;

import cpw.mods.fml.common.registry.GameRegistry;
import tybo96789.energizedquartz.EQ_Core;
import tybo96789.energizedquartz.blocks.Cable;
import tybo96789.energizedquartz.blocks.ControllerExtender;
import tybo96789.energizedquartz.blocks.CrystalOscillator;
import tybo96789.energizedquartz.blocks.Generator;
import tybo96789.energizedquartz.blocks.Machine;
import tybo96789.energizedquartz.energy.EQ_Energy;
import tybo96789.energizedquartz.energy.EnergyNet;
import tybo96789.energizedquartz.energy.EnergyPacket;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

//TODO Code Cleanup 

public class TileEnergy extends TileEntity
{
    private TileEnergy up, down, north, south, east, west, source, receptor;

    public boolean upExists, downExists, northExists, southExists, eastExists, westExists, isSource, isReceptor, isLine, foundSource = false, eNetChange =true, isController, isExtender;

    private EQ_Energy eQ_Energy;
    
    private int debug = 0, updateTimer = 0;
    
    private Block blk;
    
    private TileEnergy lastUsed = null; //RM
    
    private ForgeDirection lastChecked;
    
    private TileEnergy[] tileSides;  //Stores all the sides of the block that are TileEnergy type if not they are null
    
    private EnergyNet eNetData;
    
    private EnergyPacket packet;
    
    private static boolean forcedUpdate = false;
    
    public TileEnergy(Block block)
    {
    	this.blk =block;
    	this.eQ_Energy = new EQ_Energy(this.blk);
    	init();
    }

    @Override
    public void updateEntity()
    {
    	
    	
		this.up = getTileVoltageUp(this);

		this.down = getTileVoltageDown(this);

    	this.north = getTileVoltageNorth(this);

    	this.south = getTileVoltageSouth(this);

    	this.east = getTileVoltageEast(this);

    	this.west = getTileVoltageWest(this);
        
        this.updateSidesArray(up, down, north, south, east, west);

        if (updateTimer <= 0 || forcedUpdate)
        {
        	//updateSides();
        	/*
        	if(isSource) 
        	{
        		eNet = EnergyNet.mapNetwork(this);
        		this.eNetData = new EnergyNet(eNet);
        	}
        	*/

        	if((isExtender || isController) && this.eNetData == null) this.eNetData = new EnergyNet(this);
        	if((isExtender || isController) && this.eNetData != null) eNetData.update();
            //updateSrc(this); //Updates the receptors sources
           //System.out.println("Updated Sources");
        	//eNetChange = false;
            updateTimer = 80;
            if(forcedUpdate) forcedUpdate = false;
            //Add finds path to source
        }
        
        if(debug <= 0)
        {
        	//debug();
        
            //System.out.println(this.blk.getUnlocalizedName() +": "+ upExists +" "+ downExists +" "+ northExists +" "+ southExists +" "+ eastExists +" "+ westExists);
            //boolean sourceNull = this.source == null;
            //System.out.println("Source Null? "+ sourceNull );
            //boolean receptorNull = this.receptor == null;
        	//System.out.println("Receptor Null? " + receptorNull);
        	//System.out.println("Source: " + isSource);
        	//System.out.println("Receptor: " + isReceptor);
        	//System.out.println("isLine: " + isLine);
        	//System.out.println("isController: " + isController);
        	//System.out.println("ENet Size: " + this.eNetData.getENetSize()); //Calling this causes null pointer exceptions :P
        }
        debug++;
        updateTimer--;
        if(debug >= 120)
        {
        	debug = 0;
        }
    }
    
    private boolean init()
    {
    	isSource = this.blk instanceof Generator;
    	
    	isReceptor = this.blk instanceof Machine;

    	isLine = this.blk instanceof Cable;
    	
    	isController = this.blk instanceof CrystalOscillator;
    	
    	isExtender = this.blk instanceof ControllerExtender;
    	
    	return true;
    }
    
    public void tickTile()
    {
		this.up = getTileVoltageUp(this);

		this.down = getTileVoltageDown(this);

    	this.north = getTileVoltageNorth(this);

    	this.south = getTileVoltageSouth(this);

    	this.east = getTileVoltageEast(this);

    	this.west = getTileVoltageWest(this);
        
        this.updateSidesArray(up, down, north, south, east, west);
    }
    

    public static TileEnergy getTileVoltageUp(TileEntity tile)
    {
    	TileEntity up =  tile.worldObj.getBlockTileEntity(tile.xCoord, tile.yCoord + 1, tile.zCoord);
    	
    	TileEnergy temp = null;
    	
    	if(up != null){
	        if (up instanceof TileEnergy)
	        {
	            temp = (TileEnergy) up;
	        }
	        else
	        {
	            return null;
	        }
    	}
    	return temp;
    }
    public static TileEnergy getTileVoltageDown(TileEntity tile)
    {
    	TileEntity down =  tile.worldObj.getBlockTileEntity(tile.xCoord, tile.yCoord - 1, tile.zCoord);
    	
    	TileEnergy temp = null;
    	
    	if(down != null){
	        if (down instanceof TileEnergy)
	        {
	            temp = (TileEnergy) down;
	        }
	        else
	        {
	            return null;
	        }
    	}
    	return temp;
    }
    public static TileEnergy getTileVoltageNorth(TileEntity tile)
    {
    	TileEntity north = tile.worldObj.getBlockTileEntity(tile.xCoord + 1, tile.yCoord, tile.zCoord);
    	
    	TileEnergy temp = null;
    	
    	if(north != null){
	        if (north instanceof TileEnergy)
	        {
	            temp = (TileEnergy) north;
	        }
	        else
	        {
	            return null;
	        }
    	}
    	return temp;
    }
    public static TileEnergy getTileVoltageSouth(TileEntity tile)
    {
    	TileEntity south =  tile.worldObj.getBlockTileEntity(tile.xCoord - 1, tile.yCoord, tile.zCoord);
    	
    	TileEnergy temp = null;
    	
    	if(south != null){
	        if (south instanceof TileEnergy)
	        {
	            temp = (TileEnergy) south;
	        }
	        else
	        {
	            return null;
	        }
	        
    	}
    	return temp;
    }
    public static TileEnergy getTileVoltageEast(TileEntity tile)
    {
    	TileEntity east = tile.worldObj.getBlockTileEntity(tile.xCoord, tile.yCoord, tile.zCoord + 1);
    	
    	TileEnergy temp = null;
    	
    	if(east != null){
	        if (east instanceof TileEnergy)
	        {
	            temp = (TileEnergy) east;
	        }
	        else
	        {
	            return null;
	        }
    	}
    	return temp;
    }
    public static TileEnergy getTileVoltageWest(TileEntity tile)
    {
    	TileEntity west = tile.worldObj.getBlockTileEntity(tile.xCoord, tile.yCoord, tile.zCoord - 1);
    	
    	TileEnergy temp = null;
    	
    	if(west != null){
	        if (west instanceof TileEnergy)
	        {
	            temp = (TileEnergy) west;
	        }
	        else
	        {
	            return null;
	        }
    	}
    	return temp;
    }
    
    /*
     * This updates the SidesArray 
     */
    public void updateSidesArray(TileEnergy up, TileEnergy down ,TileEnergy north ,TileEnergy south ,TileEnergy east ,TileEnergy west)
    {
    	tileSides = new TileEnergy[] {up,down,north,south,east,west};
    }
    
    public TileEnergy[] getTileSides()
    {
    	return this.tileSides;
    }
    
    public EnergyNet getEnergyNet()
    {
    	return this.eNetData;
    }
    /*
     * This method is suppose to detect Changes in the Enet and if it has Changes then call for a network mapping update
     * This method is currently broken :P
     */
    @Deprecated
    private void updateSides()
    {
    	if(this.up != getTileVoltageUp(this))
    	{
    		eNetChange = true;
    		this.up = getTileVoltageUp(this);
    	}
    	if(this.down != getTileVoltageDown(this))
    	{
    		eNetChange = true;
    		this.down = getTileVoltageDown(this);
    	}
        if(this.north != getTileVoltageNorth(this))
        {
        	eNetChange = true;
        	this.north = getTileVoltageNorth(this);
        }
        if(this.south != getTileVoltageSouth(this))
        {
        	eNetChange = true;
        	this.south = getTileVoltageSouth(this);
        }
        if(this.east != getTileVoltageEast(this))
        {
        	eNetChange = true;
        	this.east = getTileVoltageEast(this);
        }
        if(this.west != getTileVoltageWest(this))
        {
        	eNetChange = true;
        	this.west = getTileVoltageWest(this);
        }
    }
    
    public TileEnergy getIntanceTile()
    {
    	return this;
    }
    
    public TileEnergy getUp() {
		return this.up;
	}

	public TileEnergy getDown() {
		return this.down;
	}

	public TileEnergy getNorth() {
		return this.north;
	}

	public TileEnergy getSouth() {
		return this.south;
	}

	public TileEnergy getEast() {
		return this.east;
	}

	public TileEnergy getWest() {
		return this.west;
	}
	
	public void packet(EnergyPacket ep)
	{
		this.packet = ep;
	}
	
	public EnergyPacket getEnergyPacket()
	{
		return this.packet;
	}
	public static void forceUpdate()
	{
		System.out.println("Forcing Update");
		forcedUpdate = true;
	}

	@Deprecated
	private void debug()
    {
        if (up != null)
        {
            upExists = true;
        }
        else
        {
            upExists = false;
        }

        if (down != null)
        {
            downExists = true;
        }
        else
        {
            downExists = false;
        }

        if (north != null)
        {
            northExists = true;
        }
        else
        {
            northExists = false;
        }

        if (south != null)
        {
            southExists = true;
        }
        else
        {
            southExists = false;
        }

        if (east != null)
        {
            eastExists = true;
        }
        else
        {
            eastExists = false;
        }

        if (west != null)
        {
            westExists = true;
        }
        else
        {
            westExists = false;
        }

        //System.out.println(upExists +" "+ downExists +" "+ northExists +" "+ southExists +" "+ eastExists +" "+ westExists);
    }
}
