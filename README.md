#The Knapsack Problem

The knapsack problem is a classic NP-complete problem that has been studied extensively in computer science.  

One way to solve this problem is to do an exhaustive search through all the possible combinations of objects in the knapsack, compare the weights and values, and choose the combination of objects that fits within the capacity and has the greatest value.  This takes a long time, computationally.

A less computationally-expensive method is to use a __genetic algorithm__.  The genetic algorithm may not find the very best answer, but in most cases it finds a very good solution in a reasonable amount of time.

For this problem, a list of integers that are 0 or 1 to represent your population of possible solutions.  
For example, assuming that you have eight objects, the list {0, 1, 0, 1, 1, 0, 0, 1} means that you are taking the items 2, 4, 5, and 8 in the knapsack.

Here are the basic steps in the genetic algorithm:

1.	Generate a random population of m chromosomes.

2.	Repeated change the population until your fitness criteria is satisfied.

      a.	Evaluate the fitness of each chromosome x in the population using a fitness function of your own creation.

      b.	Select the two most-fit chromosomes from the population.  

      c.	Using a crossover probability (that you create yourself), “cross over” the parents to form a new offspring.  (You should determine your own probability.)

      d.	Using a mutation probability (that you create yourself), determine if each position in the chromosome should be mutated or not.  The mutation probability should be low.

      e.	Place the new offspring in the population.

3.	When the end condition is satisfied, return the best solution in the final population of chromosomes.

A file called knapsack.txt, has these values in the order given:

```txt
number of objects N

capacity

object 1’s weight

object 1’s value

object 2’s weight

object 2’s value

…

object N’s weight

object N’s value```

