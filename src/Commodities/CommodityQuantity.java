package Commodities;

public class CommodityQuantity
{
    private Commodity commodity;
    private int quantity;

    public CommodityQuantity(Commodity commodity, int quantity)
    {
        this.commodity = commodity;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public Commodity getCommodity() {
        return commodity;
    }
    
    public boolean modifyQuantity(int mod) {
        if(quantity + mod >= 0) {
            quantity += mod;
            return true;
        }
        return false;
    }
    
    public double getTotalMass() {
        return quantity * commodity.getMass();
    }
}
