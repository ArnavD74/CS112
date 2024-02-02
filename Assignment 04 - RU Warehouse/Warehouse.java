package warehouse;

/*
 *
 * This class implements a warehouse on a Hash Table like structure, 
 * where each entry of the table stores a priority queue. 
 * Due to your limited space, you are unable to simply rehash to get more space. 
 * However, you can use your priority queue structure to delete less popular items 
 * and keep the space constant.
 * 
 * @author Ishaan Ivaturi
 */
public class Warehouse {
    private Sector[] sectors;

    // Initializes every sector to an empty sector
    public Warehouse() {
        sectors = new Sector[10];

        for (int i = 0; i < 10; i++) {
            sectors[i] = new Sector();
        }
    }

    /**
     * Provided method, code the parts to add their behavior
     * 
     * @param id     The id of the item to add
     * @param name   The name of the item to add
     * @param stock  The stock of the item to add
     * @param day    The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void addProduct(int id, String name, int stock, int day, int demand) {
        evictIfNeeded(id);
        addToEnd(id, name, stock, day, demand);
        fixHeap(id);
    }

    /**
     * Add a new product to the end of the correct sector
     * Requires proper use of the .add() method in the Sector class
     * 
     * @param id     The id of the item to add
     * @param name   The name of the item to add
     * @param stock  The stock of the item to add
     * @param day    The day of the item to add
     * @param demand Initial demand of the item to add
     */

    // public Product(int i, String n, int s, int l, int d) {
    // id = i;
    // name = n;
    // stock = s;
    // lastPurchaseDay = l;
    // demand = d;
    // popularity = l + d;
    // }

    private void addToEnd(int id, String name, int stock, int day, int demand) {

        Product newProd = new Product(id, name, stock, day, demand);
        int last = id % 10;
        sectors[last].add(newProd);

    }

    /**
     * Fix the heap structure of the sector, assuming the item was already added
     * Requires proper use of the .swim() and .getSize() methods in the Sector class
     * 
     * @param id The id of the item which was added
     */
    private void fixHeap(int id) {

        int last = id % 10;
        int size = sectors[last].getSize();
        sectors[last].swim(size);

    }

    /**
     * Delete the least popular item in the correct sector, only if its size is 5
     * while maintaining heap
     * Requires proper use of the .swap(), .deleteLast(), and .sink() methods in the
     * Sector class
     * 
     * @param id The id of the item which is about to be added
     */
    private void evictIfNeeded(int id) {

        int last = id % 10;
        // array of 10 sectors
        // one sector holds 5 product types
        if (sectors[last].getSize() >= 5) {
            sectors[last].swap(1, 5); // move to end
            sectors[last].deleteLast(); // remove end
            sectors[last].sink(1); // sink to fit pq
        }
    }

    /**
     * Update the stock of some item by some amount
     * Requires proper use of the .getSize() and .get() methods in the Sector class
     * Requires proper use of the .updateStock() method in the Product class
     * 
     * @param id     The id of the item to restock
     * @param amount The amount by which to update the stock
     */
    public void restockProduct(int id, int amount) {

        int last = id % 10;
        int size = sectors[last].getSize();

        for (int i = 1; i <= size; i++) {
            if (sectors[last].get(i).getId() == id) { // if product found
                sectors[last].get(i).updateStock(amount); // only update quantity of that prod
                return; // do not continue
            }
        }

    }

    /**
     * Delete some arbitrary product while maintaining the heap structure in O(logn)
     * Requires proper use of the .getSize(), .get(), .swap(), .deleteLast(),
     * .sink() and/or .swim() methods
     * Requires proper use of the .getId() method from the Product class
     * 
     * @param id The id of the product to delete
     */
    public void deleteProduct(int id) {

        int last = id % 10;
        int size = sectors[last].getSize();

        for (int i = 1; i <= size; i++) {
            if (sectors[last].get(i).getId() == id) { // if prod found
                sectors[last].swap(i, sectors[last].getSize()); // swap to the end
                sectors[last].deleteLast(); // remove end
                sectors[last].sink(i); // sink to fit pq
                return;
            }
        }

    }

    /**
     * Simulate a purchase order for some product
     * Requires proper use of the getSize(), sink(), get() methods in the Sector
     * class
     * Requires proper use of the getId(), getStock(), setLastPurchaseDay(),
     * updateStock(), updateDemand() methods
     * 
     * @param id     The id of the purchased product
     * @param day    The current day
     * @param amount The amount purchased
     */
    public void purchaseProduct(int id, int day, int amount) {

        int last = id % 10;
        int size = sectors[last].getSize();

        for (int i = 1; i <= size; i++) {
            if (sectors[last].get(i).getId() == id) { // if prod found
                if (sectors[last].get(i).getStock() >= amount) { // if stock available
                    sectors[last].get(i).setLastPurchaseDay(day); // update purchase day
                    sectors[last].get(i).updateDemand(amount); // increase demand
                    sectors[last].get(i).updateStock(amount * (-1)); // decrease stock
                    sectors[last].sink(i); // sink to update pq
                }
            }
        }
    }

    /**
     * Construct a better scheme to add a product, where empty spaces are always
     * filled
     * 
     * @param id     The id of the item to add
     * @param name   The name of the item to add
     * @param stock  The stock of the item to add
     * @param day    The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void betterAddProduct(int id, String name, int stock, int day, int demand) {

        int size = id % 10;

        if (sectors[size].getSize() < 5) { // if there is space
            addProduct(id, name, stock, day, demand); // add prod normally
        } else {
            for (int i = size + 1; i % 10 != size; i++) {

                if (i == 10) {
                    i = 0;
                }

                if (sectors[i].getSize() < 5) {
                    addProduct(((id / 10) * 10 + i), name, stock, day, demand);
                    return;
                }
            }

            addProduct(id, name, stock, day, demand);
        }

    }

    /*
     * Returns the string representation of the warehouse
     */
    public String toString() {
        String warehouseString = "[\n";

        for (int i = 0; i < 10; i++) {
            warehouseString += "\t" + sectors[i].toString() + "\n";
        }

        return warehouseString + "]";
    }

    /*
     * Do not remove this method, it is used by Autolab
     */
    public Sector[] getSectors() {
        return sectors;
    }
}