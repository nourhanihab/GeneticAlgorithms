import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class GA {
	public static void main(String[] args)throws Exception 
	  { 
	
	  Scanner scanner = new Scanner(new File("input_example.txt"));
	  BufferedReader input = new BufferedReader(new FileReader(new File("input_example.txt")));
	  int C = 0, N = 0 , S = 0;
	  int selections = 4 ; //selections of random
	  String[] splitted = null; 
	  int []weight=null;
	  int []benefit=null;
	  C = scanner.nextInt(); //number of test cases
	  for (int i = 0 ; i < C ; i++){
		//  System.out.println("Case: " + (i+1) );
		  String temp = null;	  
		  N = scanner.nextInt(); 		  
		  S = scanner.nextInt(); 
		  weight = new int[N];
		  benefit = new int[N];
		 // System.out.println("N "+ N);
		  //System.out.println("S " + S);
		  for (int j=0 ; j< N ; j++){
			  if (scanner.hasNext()){
				weight[j]=scanner.nextInt();
				benefit[j]=scanner.nextInt(); 
			  }
		  }
		  knapsack(N,S,weight,benefit,selections, i);
	  }
	  
	  } 

	public static String randompopulation(int N){
		String binary="";
		int range=(int) Math.pow(2,N)-1;
	    Random rg = new Random();
	    int n = rg.nextInt(range);
	    String binaryrep  = toBinary(n);
	    //System.out.println("random " + n);
	    //System.out.println("Binary representation " + binaryrep);
	    if(binaryrep.length()<=N){
	    	for (int i =0 ; i<N-binaryrep.length();i++){
	    		binary+="0";
	    	}
	    	binary+=binaryrep;
	    }
	   // System.out.println("Binary after editing " + binary);
	    return binary;
	}
	public static String toBinary(int rand)
	{
	    String binary = "";
	    while (rand > 0)
	    {
	        binary =  ( (rand % 2 ) == 0 ? "0" : "1") +binary;
	        rand = rand / 2;
	    }
	    return binary;
	}
	public static int fitnessfunction(String binary,int [] weight, int[]benefit){
		int w=0,b=0;
		//System.out.println("BINARYLENGTH  ++" + binary.length());
		for (int i=0 ; i< binary.length() ; i++){
			//System.out.println(weight[i] +"+++++" + binary.charAt(i));
			if (binary.charAt(i) == '1'){
				w+=weight[i];
				b+=benefit[i];
			}
			else {
				w+=0;
				b+=0;
			}
		}
		//System.out.println("weight " + w +" benefit " + b);
		
		return b;
	}
	public static String[] selection(String [] binary, int[] weights, int[] benefits, int N){
		
		int totalweight = 0;
        String[] choosenelements = new String[N];
        for (int i = 0; i < benefits.length; i++) {
            totalweight += benefits[i];
           // System.out.println("BENEFF "+ benefits[i]);
        }
        Random rg = new Random();
        for (int i = 0 ; i < N; i++) {
        	int partialsum=0, random, index = 0, lastAddedIndex = 0;
        	//System.out.println("TOTAL WEIGHT "+ totalweight);
        	random = rg.nextInt(totalweight-1);
			while(partialsum < random && index < N) {
				//System.out.println("IND " + index);
				partialsum+= weights[index];
				lastAddedIndex = index;
                index++;
			}
			choosenelements[i]=binary[lastAddedIndex];
        }
        return choosenelements;
	}
	public static String [] crossover(String[] parents){
		String [] offsprings = new String [parents.length];
		double pc = 0.7;
		int pointer1,pointer2;
		String off1, off2;
	    Random rg = new Random();
	    pointer1 = rg.nextInt(parents[0].length());
	    for (int i =0 ; i< parents.length; i+=2){
	    	if(generateRandom(1,0) > pc) {
    			offsprings[i] = parents[i];
        		offsprings[i+1] = parents[i+1];
        		continue;
    		}
	    	off1 = parents[i].substring(0,pointer1) + parents[i+1].substring(pointer1,parents[i+1].length());
	    	off2 = parents[i+1].substring(0,pointer1)+ parents[i].substring(pointer1,parents[i].length());
	    	offsprings[i]=off1;
	    	offsprings[i+1]=off2;
	    	
	    }
		return offsprings;
				
		
	}
    public static double generateRandom(double max , double min) {
    	Random randomNum = new Random();
    	double random = randomNum.nextDouble() * (max-min) + min;
    	return random;
    }
	public static String [] mutation(String [] individuals){
		String [] mutated = new String[individuals.length];
		 for (int i =0 ; i<individuals.length; i++){
			 mutated[i]=mutationforonestring(individuals[i]);
		 }
		return mutated;
		
	}
	public static String mutationforonestring (String chromo){
		  double pm = 0.0;
		  char [] chromosomes = chromo.toCharArray();
		  for (int i=0 ; i< chromosomes.length ; i++){
			  pm = generateRandom(0.0,1);
			  if (pm<0.02){//my restriction you can change it from 0.001 to 0.1
				  if (chromosomes[i]=='1'){
					  chromosomes[i]='0';
				  }
				  else{
					  chromosomes[i]='1';
				  }
			  }
			  else {
				  continue;
			  }
		  }
		  String mutated = new String(chromosomes);
		  return mutated;		
	}

	
public static String[] elitismreplacement (int [] w, int[] b,String[] mutatedfinal,String [] parents){
	//System.out.println("Elements Before sorting:- ");
	//for (int i=0 ; i<mutatedfinal.length ;i++){
	//	System.out.println( "IND: " +mutatedfinal[i] + " W:" +fitnessfunction(mutatedfinal[i],w,b));
	//}
	Sortforelitism (mutatedfinal,w,b);
	//System.out.println("Elements After sorting:- ");
	//for (int i=0 ; i<mutatedfinal.length ;i++){
	//	System.out.println("IND: " + mutatedfinal[i] + " W:"+ fitnessfunction(mutatedfinal[i],w,b));
	//}
	// Remove lowest
    int index = 0;
    String [] newGeneration = parents ;
	for (int j = newGeneration.length -1; j > 0 && index < mutatedfinal.length ; j--) {
		newGeneration[j] = mutatedfinal[index];
		index++;
    }
	return newGeneration;

	
	
}	
public static boolean individualisValid (String binary , int [] weights, int maxweight){
	int b = 0;
    for (int i = 0; i < binary.length(); i++) {
        if (binary.charAt(i) == '1') {
            b += weights[i];
        }
    }
    //System.out.println("MAX: " + maxweight + "CURR " + b);
	return (b<=maxweight);
}	
public static String bestIndividual( String [] individuals, String current, int[] benefits, int[]weights, int maxweight){
	String best = current;
	int maxBenefit = fitnessfunction(current,weights,benefits), currentBenefit;
	//System.out.println("MAX : "+maxBenefit);
	for (int i = 0; i < individuals.length; i++) {
		if(individualisValid(individuals[i], weights,maxweight)){
			currentBenefit = fitnessfunction(individuals[i],weights, benefits);
			if( currentBenefit > maxBenefit ){
				maxBenefit = currentBenefit;
				best = individuals[i];
			}
		}
    }
	return best;
}
public static void Sortforelitism (String [] individuals, int[] weights, int[] benefits){
	int n = individuals.length; 
    for (int i = 0; i < n-1; i++) 
        for (int j = 0; j < n-i-1; j++) 
            if (fitnessfunction(individuals[j],weights,benefits) < fitnessfunction(individuals[j+1], weights,benefits)) {
                String tempS = individuals[j]; 
                individuals[j] = individuals[j+1]; 
                individuals[j+1] = tempS;   
            }
}
public static long encodetodecimal (String finall){
	long decimal= Long.parseLong(finall,2);  
	return decimal;
}
public static void knapsack(int N, int S, int[]weight,int[]benefit, int selections, int casee) {
	//System.out.println("Case: " + (casee+1) );
	String [] binary;
	String best="";
	String [] choosen,offsprings,mutated;
	int [] weights =  new int [N];
	int[] benefits =  new int [N];
	boolean valid = false;
	choosen = new String [benefit.length];
	offsprings = new String [selections];
	binary = new String [selections];	
	mutated = new String [selections];
	String binary1 = null;
	for (int i=0 ; i<selections ; i++){
		binary1=randompopulation(N);
		binary[i]=binary1;
		//int weightt=fitnessfunction(binary1,weight,benefit);		
		}
	//check if solution is not valid repeat the best individual one more time 
		for (int i =0 ; i< binary.length ; i++){
		//System.out.println("S "+S);
		valid = individualisValid(binary[i],weight,S);
		//System.out.println("BEST Valid "+ valid + binary[i]);
		if (valid == false ){
			best = bestIndividual(binary,binary[i],benefit, weight, S);
			binary[i]=best;
		}
	
		}
	
		//for (int i =0 ; i<binary.length; i++){
		//System.out.println(binary[i] + "+++++" + fitnessfunction(binary[i],weight,benefit));
		//}
		String [] finall = new String [benefit.length];
		choosen = selection(binary,weight,benefit,selections);
		offsprings = crossover(choosen);
		mutated = mutation(offsprings);
		finall = elitismreplacement(weight,benefit,mutated,binary);
 
		for (int i = 0; i<mutated.length; i++){
			best = bestIndividual(mutated,mutated[i],benefit, weight, S);	
		}
		int items=0;
		int b=0;
		for (int i =0 ; i<best.length(); i++){
			if (best.charAt(i)=='1')
			items++;
			b+=weight[i];
		}
		System.out.println("Case " + (casee+1)+": benefit: " +fitnessfunction(best,weight,benefit));
	
}

}
