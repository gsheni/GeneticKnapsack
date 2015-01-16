import java.util.Arrays; // making arrays, and sorting
import java.util.Random; // random generator
import java.util.Scanner; // waiting user input
import java.io.BufferedReader; // parsing the text file
import java.io.FileInputStream; //inputing the file
import java.io.IOException; //input/output exception requirement
import java.io.File; // using the File.seperator
import java.io.InputStreamReader; // for parsing the text file. 
/*
 * Gaurav Sheni
 * November 25, 2014
 * CSC 371
 * Project 4 - Genetic Algorithm
 */

public class GeneticKnapsack {
     public static void main(String[] args) throws IOException {
    	 //for reading the file in and putting into into the correct array
			File directory = new File(".");
			File finalpath = new File(directory.getCanonicalPath() + File.separator + "\\src\\knapsack.txt");
			FileInputStream pathstream = new FileInputStream(finalpath);
				BufferedReader br = new BufferedReader(new InputStreamReader(pathstream));
			String line = null; // read in strings, convert later
			line = br.readLine(); //read next line
			int numberofobjects = Integer.parseInt(line); //convert string to int
			line = br.readLine(); //read next line
			int capacity = Integer.parseInt(line);
			int[] weights = new int[numberofobjects];
			int[] values = new int[numberofobjects];
			int c = 0; //counter for putting things into the array
			while ((line = br.readLine()) != null) {
				weights[c] = Integer.parseInt(line);
				line = br.readLine();
				values[c] = Integer.parseInt(line);
				c++;
			}
			br.close(); //close bufferedreader
			
			//Ask user input
			System.out.print("Do you want to print he weights, values, number of objects, and capcity?");
			System.out.print("\nEnter 1 for yes, and 0 for no.");
			Scanner input = new Scanner( System.in );
			int useranswer = input.nextInt(); 
			
			//if the user wants details, print them, only if the answer 1
			if(useranswer == 1 ){
	      		System.out.print("Capacity is " + capacity+"\n");
	      		System.out.print("Number of Objects is " + numberofobjects);
	       		for(int i = 0; i<numberofobjects;i++){
	           		System.out.print("\n");
		    		System.out.print("Weight is " + weights[i]+ " who's value is " + values[i] );
	       		}
			}
			
			//Create random population of m
			Random rand = new Random();
			//the m is less than the number of objects, n, because it is half the number of objects
			int m = numberofobjects / 2;
			//int m = numberofobjects / 4; // for 1/4 of n
			int[][] population = new int[m][numberofobjects]; //create 2D array with m * m
	
			for(int i = 0 ; i < population.length ; i++){
				for(int j = 0; j < numberofobjects ; j++){
					population[i][j] = rand.nextInt(2);  //put either 0 or 1 into the 2d array
				}
			}
						
			int zeros = 0; //checking if a chromosome is just 0s
			for(int i = 0 ; i < population.length ; i++){
				for(int j = 0; j < population[0].length ; j++){
					if( population[i][j] == 0){ // the the value is 0 add to zeros counter
						zeros++;
					}
					if(zeros == population[0].length){ // this means all genes are 0s
						int changeindexy = rand.nextInt(numberofobjects - 1) + 1; // get a random value between the beginning and end of chromosome
						population[i][changeindexy] = 1;// this assigns 1 to a random index in a list if the whole list is 0s	    					
					}
				}
				zeros = 0; //put back to zero, cause we are done with this chromosome
			}
			int generations = 1;// for counting how many generations it takes
			int[] fitnesses = new int[population.length]; // for storing the fitness values 
       		System.out.print("\n");
			int currentsumweights = 0;//to hold the sum of the weights
			int currentsumvalues = 0; //to hold the sum of the values	
			int[] answer = new int[numberofobjects];
			while((capacity - currentsumweights) > 4){ //make the sum weight less than 4 and then end
			//while(generations < 12){ //for using generations
				fitness(population, fitnesses, weights, values, capacity, m, numberofobjects ); //get the fitness values and put into the passed fitnesses array
	            System.out.print("\n");
				for(int[] row : population) {
		            System.out.print("|");
		            printRow(row); //print the data
		        }
				for(int k = 0 ; k < fitnesses.length ; k++){
		    		System.out.println("Fitness is " + fitnesses[k]);
				}
				int[] maxes = new int[4]; //hold to best fits
				findtwomax(fitnesses, maxes); //find the two highest fitnesses, put into maxes array
				//max[0] is first high fitness value, max[1] is second high 
				//max[2] is location of of first high fitness value in relation to the fitness array which corlates to population 2d array
				//max[3] is same as above but for the second high fitness value
				int[] parent1 = new int[numberofobjects]; //hold chromosome of parent 1
				int[] parent2 = new int[numberofobjects]; //hold chromosome of parent 2
				int parent1location = 0; //to know where we took out parent 1, so we can put it back later
				int parent2location = 0; //to konw where we took out parent 2
	
				for(int i = 0 ; i < population.length ; i++){
					for(int j = 0; j < population[0].length ; j++){
						if(i == maxes[2]){
							parent1[j] = population[i][j]; //get the first parent chromosome
							parent1location = i; //mark location
						}
						else if(i == maxes[3]){
							parent2[j] = population[i][j]; //get the second parent chromosome
							parent2location = i; //mark location
						}
					}
				}
				//print two best 
	    		System.out.print("The two best chromsomes, who are the parents:\n");
	    		System.out.print("Parent 1:\n ");
		    	for(int i = 0 ;i < parent1.length ; i++){
		    		System.out.print(parent1[i] +"  ");
		    	}
		    	System.out.print("\n");
	    		System.out.print("Parent 2:\n ");
		    	for(int i = 0 ;i < parent2.length ; i++){
		    		System.out.print(parent2[i] +"  ");
		    	}
		    	System.out.println("\n");
				
		    	//Crossover
		    	//if( rand.nextDouble() <= 0.80 ) { 
		    	//First technique for child 1
		    	//find random index to star the crossover at
				int splitpoint = rand.nextInt(((numberofobjects-1)+1) - 2) + 2;
				int[] child1 = new int[numberofobjects]; //hold chromosome of child 1
				int[] child2 = new int[numberofobjects]; //hold chromosome of child 2
	    		for( int i = 0 ; i < splitpoint ; i++){
	    			child1[i] = parent1[i]; //put part before the split point from first parent
	    		}
	    		for( int i = splitpoint; i < parent1.length ; i++){
	    			child1[i] = parent2[i]; //put second part with other parent 
	    		}
		    	//First technique for child 2
	      		for( int i = 0 ; i < parent2.length ; i++){
	    			if(parent1[i] == parent2[i]){
	    				child2[i] = parent1[i]; //if same for both parents, then transfer to child
	    			}
	    			else{// if different, 50/50 that it will be 0 or 1
	    				boolean temp = rand.nextBoolean(); //use boolean to get random 0 or 1
	    				if(temp == true){
	    					child2[i] = 1;
	    				}
	    				else{
	    					child2[i] = 0;
	    				}
	    			}
	    		}
	      		//Print children
	    		System.out.print("After crossover, Child 1: \n ");
		    	for(int i = 0 ;i < child1.length ; i++){
		    		System.out.print(child1[i] +"  ");
		    	}
		    	System.out.print("\n");
	    		System.out.print("After crossover, Child 2: \n ");
		    	for(int i = 0 ;i < child2.length ; i++){
		    		System.out.print(child2[i] +"  ");
		    	}
		    	System.out.println("\n");

		    	//Mutation
		    	//if( rand.nextDouble() <= 0.10 ) { 
		    	int choosen = rand.nextInt(((numberofobjects-1)+1) - 1) + 1;
		    	//choose gives you a random number bettween 1 and 6
				if(child1[choosen] == 0){ //if the choosen index into child 1 is 0 then change to 1
					child1[choosen] = 1; 
				}
				else if(child1[choosen] == 1){ //if the choosen index into child 1 is 1 then change to 0
					child1[choosen] = 0;
				}
		    	choosen = rand.nextInt(((numberofobjects-1)+1) - 1) + 1;
				if(child2[choosen] == 0){ //same as above but for child 2
					child2[choosen] = 1;
				}
				else if(child2[choosen] == 1){ //same as above but for child 2
					child2[choosen] = 0;
				}
				//do cloning
				/* if no mutation or crossover happens, need to writing in booleans 
				if(mutation == false && crossover == false){
					child1 = parent1.clone();
					child2 = parent2.clone();				
				}
				*/
	    		System.out.print("After mutation, Child 1: \n ");
		    	for(int i = 0 ;i < child1.length ; i++){
		    		System.out.print(child1[i] +"  ");
		    	}
		    	System.out.print("\n");
	    		System.out.print("After mutation, Child 1: \n ");
		    	for(int i = 0 ;i < child2.length ; i++){
		    		System.out.print(child2[i] +"  ");
		    	}
		    	System.out.println("\n");
		    			    	
		    	//Replacing population
		    	//Create random population of m
		    	//Fill randomly with 0 or 1
				for(int i = 0 ; i < population.length ; i++){
					for(int j = 0; j < numberofobjects ; j++){
						population[i][j] = rand.nextInt(2); 
					}
				}
										
				zeros = 0;//checking if a chromosome is just 0s
				for(int i = 0 ; i < population.length ; i++){
					for(int j = 0; j < population[0].length ; j++){
						if( population[i][j] == 0){ // the the value is 0 add to zeros counter
							zeros++;
						}
						if(zeros == population[0].length){ // this means all genes are 0s
							int changeindexy = rand.nextInt(numberofobjects - 1) + 1; // get a random value between the beginning and end of chromosome
							population[i][changeindexy] = 1;// this assigns 1 to a random index in a list if the whole list is 0s	    					
						}
					}
					zeros = 0; 
				}
				
		    	//replace with best parent, and with the better child
		    	int[] childfitness = new int[2]; //for holding 2 fitness values of children and 2 index values
				childfitness = fitnesschild(child1, child2, weights, values, capacity, m,  numberofobjects); //get fitness values of 2 children
		    	int[] parentfitness = new int[2];//for holding 2 fitness values of parent and 2 index values
				parentfitness = parentfitness(parent1, parent2, weights, values, capacity, m,  numberofobjects); //get fitness values of 2 parents

				int maxchildlocation = 0; //make the first fitness the biggest one by default
				if(childfitness[1] > childfitness[0]){ //if its not the biggest one, then change to other
					maxchildlocation = 1; //change the index to be used later
				}
				//now max child is child1 or child 2
				//it is child 1 if maxchildlocation l is 0 and child 2 if maxchildlocation is 1				
				int minparentlocation = 0;//make the first fitness the smallest one by default
				if(parentfitness[1] < parentfitness[0]){//if its not the smallest one, then change to other
					minparentlocation = 1;//change the index to be used later
				}

				//Print genes and fitness values of the two things to be kept into next population
	    		System.out.print("The choice of chromosomes to include in the next population are as follows");
				if(maxchildlocation == 0){
					//so its child 1
		    		System.out.print("\nChild 1 : ");
					for(int i = 0 ; i < child1.length ; i++){
						System.out.print(child1[i] +"  ");
					}
		    		System.out.print("\nChild 1 Fitness: " + childfitness[0]);
				}
				if(maxchildlocation == 1){
					//so its child 2	
					System.out.print("\nChild 2 : ");
					for(int i = 0 ; i < child2.length ; i++){
						System.out.print(child2[i] +"  ");
					}
		    		System.out.print("\nChild 2 Fitness: " + childfitness[1]);

				}
				if(minparentlocation == 0){
					//so its parent 1
					System.out.print("\nParent 1 : ");
					for(int i = 0 ; i < parent1.length ; i++){
						System.out.print(parent1[i] +"  ");
					}
		    		System.out.print("\nParent 1 Fitness: " + parentfitness[0]);
				}
				if(minparentlocation == 1){
					//so its parent 2
					System.out.print("\nParent 2 : ");

					for(int i = 0 ; i < parent2.length ; i++){
						System.out.print(parent2[i] +"  ");
					}
		    		System.out.print("\nParent 2 Fitness: " + parentfitness[1]);
				}	    		    		
			
				//using the parent1location from when we extracted the parent, use them to insert
				//the same goest for parent2location
	    		for(int i = 0 ; i < population.length ; i++){
					for(int j = 0; j < population[0].length ; j++){
						if(maxchildlocation == 0){//use max child
							//so its child 1
							if(i == parent1location){
								population[i][j] = child1[j];
							}
						}
						if(maxchildlocation == 1){ //use max child
							//so its child 2							
							if(i == parent1location){
								population[i][j] = child2[j];
							}
						}
						if(minparentlocation == 0){ //use min child
							//so its child 1
							if(i == parent2location){
								population[i][j] = parent1[j];
							}
						}
						if(minparentlocation == 1){ //use min child
							//so its child 1
							if(i == parent2location){
								population[i][j] = parent2[j];
							}
						}
					}
				}
				generations++; //increment generation
				fitness(population, fitnesses, weights, values, capacity, m, numberofobjects ); //recalculate fitness 
				int[] mostfit = new int[4];
				findmax(fitnesses, mostfit); //find the most fit ONE
				answer = new int[numberofobjects];
				for(int i = 0 ; i < population.length ; i++){
					for(int j = 0; j < population[0].length ; j++){
						if(i == mostfit[1]){ //use mostfit[1] because thats the location in fitness where the max value was found
							answer[j] = population[i][j]; //Extract the most fit ONE
							//population[i][j] = 2;
						}
					}
				}
				currentsumweights = 0; //initialize to 0 because we need to recalculate for the while condition
				currentsumvalues = 0; //initialize to 0 because we need to recalculate for the while condition
	 			int[] answerfitneses = new int[4]; //
				 for(int i = 0 ;i < answer.length ; i++){
						if(answer[i] == 1){
							currentsumweights = currentsumweights + weights[i];
							currentsumvalues = currentsumvalues + values[i];
						}
				}
					if(currentsumweights <= capacity){
						fitnesses[0] =  currentsumvalues;
					}
					else{
						fitnesses[0] =  -10 * (currentsumweights - capacity);
					}
			}

            System.out.print("\n\n");
			for(int[] row : population) {
	            System.out.print("|");
	            printRow(row); //Print result for user
	        }
    		System.out.print("\nMost Fit Genes are :\n ");
	    	for(int i = 0 ;i < answer.length ; i++){
	    		System.out.print(answer[i] +"  "); //print best ONE
	    	}
    		System.out.print("\n");
       		System.out.print("Capacity is " + capacity);
       		System.out.print("\n");
    		System.out.print("Weight is " + currentsumweights );
       		System.out.print("\n");
    		System.out.print("Value is " + currentsumvalues );
       		System.out.print("\n");
    		System.out.print("Generations: " + generations );
     }
     public static void printRow(int[] row) { //for printing the m chromosome
         for (int i : row) {
             System.out.print(i);
             System.out.print(" |");
         }
         System.out.println();
     }

     //for calculating fitness values, doesn't return anything but it changes array called fitnesses, with values inputted
     public static void fitness(int population[][], int[] fitnesses, int[] weights, int[] values, int capacity, int m, int numberofobjects){
    		for(int i = 0 ; i < m ; i++){
    			int currentsumweights = 0;
    			int currentsumvalues = 0; 
    			for(int j = 0; j < numberofobjects ; j++){
    				if(population[i][j] == 1){
    					currentsumweights = currentsumweights + weights[j]; //get sum of weights, which correspond to the weights array read in
    					currentsumvalues = currentsumvalues + values[j];//get sum of values, which correspond to the weights values read in
    				}
    			}
				if(currentsumweights <= capacity){
					fitnesses[i] =  currentsumvalues;//Fitness function is your value can get you
				}
				else{
					fitnesses[i] =  -10 * (currentsumweights - capacity);
					//other fitness blame ways below
					//fitnesses[i] =  -1 * (capacity - currentsumweights);
					//fitnesses[i] =  -1;
				}
    		}
     }
     public static int[] fitnesschild( int[] child1, int[] child2, int[] weights, int[] values, int capacity, int m, int numberofobjects){
		int currentsumweights = 0;
		int currentsumvalues = 0; 	
		int[] fitnesses = new int[4]; //to return the fitness values in an array.
		for(int i = 0 ;i < child1.length ; i++){
				if(child1[i] == 1){
					currentsumweights = currentsumweights + weights[i];//get sum of weights, which correspond to the weights array read in
					currentsumvalues = currentsumvalues + values[i];//get sum of values, which correspond to the weights values read in
				}
			}
			if(currentsumweights <= capacity){
				fitnesses[0] =  currentsumvalues;//Fitness function is your value can get you
			}
			else{
				fitnesses[0] =  -10 * (currentsumweights - capacity);
				//fitnesses[i] =  -1 * (capacity - currentsumweights);
				//fitnesses[0] =  -1;
			}
			// set the variables below to zero because now doing for child 2
		currentsumweights = 0;
		currentsumvalues = 0;
		 for(int i = 0 ;i < child2.length ; i++){
				if(child2[i] == 1){
					currentsumweights = currentsumweights + weights[i];
					currentsumvalues = currentsumvalues + values[i];
				}
			}
			if(currentsumweights <= capacity){
				fitnesses[1] =  currentsumvalues;
			}
			else{
				fitnesses[1] =  -10 * (currentsumweights - capacity);
				//other fitness blame ways below
				//fitnesses[i] =  -1 * (capacity - currentsumweights);
				//fitnesses[1] =  -1;
		
		}
		return fitnesses; //return fitnesses array
		     }
     //this function works extremely similar to above, see that
     public static int[] parentfitness( int[] parent1, int[] parent2, int[] weights, int[] values, int capacity, int m, int numberofobjects){
		int currentsumweights = 0;
		int currentsumvalues = 0; 	
		int[] fitnesses = new int[4];
    	 for(int i = 0 ;i < parent1.length ; i++){
				if(parent1[i] == 1){
					currentsumweights = currentsumweights + weights[i];
					currentsumvalues = currentsumvalues + values[i];
				}
	    	}
			if(currentsumweights <= capacity){
				fitnesses[0] =  currentsumvalues;
			}
			else{
				fitnesses[0] =  -10 * (currentsumweights - capacity);
				//other fitness blame ways below
				//fitnesses[i] =  -1 * (capacity - currentsumweights);
				//fitnesses[0] =  -1;
			}
    	 currentsumweights = 0;
 		currentsumvalues = 0;
    	 for(int i = 0 ;i < parent2.length ; i++){
				if(parent2[i] == 1){
					currentsumweights = currentsumweights + weights[i];
					currentsumvalues = currentsumvalues + values[i];
				}
	    	}
			if(currentsumweights <= capacity){
				fitnesses[1] =  currentsumvalues;
			}
			else{
				fitnesses[1] =  -10 * (currentsumweights - capacity);
				//other fitness blame ways below
				//fitnesses[i] =  -1 * (capacity - currentsumweights);
				//fitnesses[0] =  -1;
			}
			return fitnesses;
     }

     //to find the two highest fitness values
     public static void findtwomax(int[] fitnesses, int[] maxes){
    	 int[] copy = fitnesses.clone(); //make a copy of the array
    	 Arrays.sort( copy ); //sort the copied array
    	 maxes[0] = copy[fitnesses.length - 1]; //get the highest value, put into max to return
    	 maxes[1] = copy[fitnesses.length - 2]; //get the second highest value, put into max to return
    	    for(int i=0; i<fitnesses.length; i++){
    	         if(fitnesses[i] == maxes[0]){
    	        	 maxes[2] =  i; //for the location to return, put into max to return
    	         }
    	         if(fitnesses[i] == maxes[1]){
    	        	 maxes[3] =  i; //for the location to return, put into max to return
    	         }
    	    }
     }
     public static void findtwomin(int[] fitnesses, int[] mins){
    	 int[] copy = fitnesses.clone();//make a copy of the array
    	 Arrays.sort( copy );//sort the copied array
    	 mins[0] = copy[0];//get the lowest value, put into min to return
    	 mins[1] = copy[1];//get the second lowest value, put into min to return
    	    for(int i=0; i<fitnesses.length; i++){
    	         if(fitnesses[i] == mins[0]){
    	        	 mins[2] =  i;//for the location to return, put into min to return
    	         }
    	         if(fitnesses[i] == mins[1]){
    	        	 mins[3] =  i;//for the location to return, put into min to return
    	         }
    	    }
     }
	 public static void findmax(int[] fitnesses, int[] result){
		 int[] copy = fitnesses.clone();//make a copy of the array
		 Arrays.sort( copy );//sort the copied array
		 //the highest fitness is at the top
	 result[0] = copy[fitnesses.length - 1];//get the highest value, put into result to return
	for(int i=0; i<fitnesses.length; i++){
	     if(fitnesses[i] == result[0]){
	    	 result[1] =  i;//for the location to return, put into result to return
	     }
		}
     }
}
