import java.util.*;
import java.io.*;


class Collection{
	protected String category = null;
	protected double amount = 0;
	protected int count =0;
	
	public Collection(String category_name_val)
	{
		category = category_name_val;
	}
	
	public Collection() {}
	

	protected void add_amount_count(double add_amount)
	{
		this.count +=1;
		this.amount += add_amount;
		
		
	}
	
}


class Passenger{
	public String identity; 
	public double balance = 0;
	public String category = null;
	public int return_flag = 0;
	public Passenger(String identity_val,int balance_val){
		identity = identity_val;
        balance = balance_val;
		}
	public void set_category(Passenger self, String category_val) {
		self.category = category_val;
	}
}
class Station extends Collection{
	public String station_name = null;
    public int total_collection = 0;
    public int total_discount = 0;
    private Map<String,Integer> prices = new HashMap<String,Integer>();
    Collection max_collection = new Collection();
    Collection mid_collection = new Collection();
	Collection min_collection = new Collection();
	public Map<String,Collection> passenger_category = new HashMap<String,Collection>();
	
	private void initialize_prices_dictionary() 
	{
	prices.put("ADULT",200);
	prices.put("SENIOR_CITIZEN",100);
	prices.put("KID",50);

	}
	
    
    public Station(String station_name_val)
    {
    	super();
    	station_name = station_name_val;
    	
    	passenger_category.put("ADULT",new Collection("ADULT"));
    	passenger_category.put("SENIOR_CITIZEN",new Collection("SENIOR_CITIZEN"));
    	passenger_category.put("KID",new Collection("KID"));
    	
    	initialize_prices_dictionary();
    	
    	
    	
    	
    }
    
    
    
    public void update_values(Passenger passenger, String category)
    {   
    	
    	double net_price;
		double add_balance;
		double additional_cost;
    	if (passenger.return_flag == 0)
		{
			net_price = prices.get(category);
		    passenger.return_flag =1;
		    //System.out.println("forward journey, price " + net_price);
		    
		}
		
		else
		{
			net_price = prices.get(category)/2;
            passenger.return_flag = 0;
            total_discount += net_price;
            //System.out.println("return journey, price" + net_price);
			
		}
		
		if (net_price > passenger.balance)
		{
			add_balance = net_price - passenger.balance;
            additional_cost = 0.02*add_balance;
            passenger.balance = 0;
		}
		else
		{
			additional_cost = 0;
            passenger.balance = passenger.balance - net_price;
		}
		
		total_collection += (additional_cost + net_price);
		update_collection(category, additional_cost + net_price);
		
    }
    
    
    public void update_collection(String category, double add_amount)
    {
    	passenger_category.get(category).add_amount_count(add_amount);
    }
    
    public void sort_collection()
	{
		Collection temp_max = passenger_category.get("ADULT");
		Collection temp_min = passenger_category.get("ADULT");
		Collection temp_mid = passenger_category.get("ADULT");
			
		Collection kid = passenger_category.get("KID");
		Collection senior = passenger_category.get("SENIOR_CITIZEN");
			
			
			
			
			if ((kid.count > temp_max.count) || (senior.count > temp_max.count))
			{
				if (kid.count < senior.count)
				{
					temp_max = senior;
				}
				else
				{
					temp_max = kid;
				}
			}
			
			

			if ((temp_min.count >= kid.count) || (temp_min.count >= senior.count))
			{
				if (kid.count < senior.count)
				{
					temp_min = kid;
				}
				else
				{
					temp_min = senior;
				}
			}
			
			for (String key : passenger_category.keySet())
			{
				if ((key != temp_min.category) && (key != temp_max.category))
				{
					 temp_mid = passenger_category.get(key);
				}
			}
			max_collection = temp_max;
			mid_collection = temp_mid;
			min_collection = temp_min;
			
			
		}
    
    public void show_results()
	{
	
			System.out.println("TOTAL_COLLECTION " + station_name + " " + total_collection + " " + total_discount);
			System.out.println("PASSENGER_TYPE_SUMMARY");
			if (max_collection.amount != 0)
				System.out.println(max_collection.category + " " + max_collection.count);
			if (mid_collection.amount != 0)
				System.out.println(mid_collection.category + " " + mid_collection.count);
			if (min_collection.amount!= 0)
				System.out.println(min_collection.category + " " + min_collection.count);

	}
      
}


class Geektrust {
      public Passenger passenger;
      public Station station;
      static Map<String,Passenger> passenger_obj = new HashMap<String,Passenger>();
      static Map<String,Station> station_obj = new HashMap<String,Station>();
      
	
	
	

	
	public static void calculate_station_collection(List<String[]> lineslist)
	{
		
		for (String passenger : passenger_obj.keySet())
			for (String[] line : lineslist)
				if (Arrays.asList(line).contains(passenger) && Arrays.asList(line).contains("CHECK_IN"))
					station_obj.get(line[3]).update_values(passenger_obj.get(line[1]),line[2]);
		
	}
	

        
    
    public static void main(String args[]) {
       String filePath = args[0];
       List<String[]> lineslist = new ArrayList<String[]>();
 
//       String filePath = "C:\\Users\\ASUS\\Desktop\\javacode\\input1.txt";
       
       try{
           BufferedReader br = new BufferedReader(new FileReader(filePath));
       String st;

       

   
       while ((st = br.readLine()) != null)
	{
           String[] arr = st.split("\\W+");
           lineslist.add(arr);
           if (st.contains("BALANCE"))
           {
               int balance = Integer.parseInt(arr[2]);  
               passenger_obj.put(arr[1], new Passenger(arr[1],balance));
           }
           else if (st.contains("CHECK_IN"))
           {
        	   passenger_obj.get(arr[1]).set_category(passenger_obj.get(arr[1]),arr[2]);
        	   station_obj.put(arr[3], new Station(arr[3]));
        	   
           }
        	   
           
	 }
       br.close();
       }
	catch (Exception e){
    	System.out.println("no:"+ e);
	}
       
       
       
       calculate_station_collection(lineslist);
       
       station_obj.get("CENTRAL").sort_collection();
       station_obj.get("AIRPORT").sort_collection();
       
       station_obj.get("CENTRAL").show_results();
       station_obj.get("AIRPORT").show_results();
           
}}
