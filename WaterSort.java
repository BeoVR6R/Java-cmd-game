import java.util.Random;
import java.util.Scanner;

class WaterSort {
	Character top = null;
	// create constants for colors
	static Character red= new Character('r');
	static Character blue = new Character('b');
	static Character green= new Character('g');
	
	// method to show contents of all bottles
	public static void showAll( StackAsMyArrayList bottles[])
	{
		for (int i = 0; i < 5; i++)
        {
            System.out.println("Bottle "+ i + ": " + bottles[i]);
        }
	}
	
    public static void main(String args[])
    {
		// Bottles declaration
        StackAsMyArrayList bottles[] = new StackAsMyArrayList[5];
        //You can do this with a for also
        bottles[0] = new StackAsMyArrayList<Character>();
        bottles[1] = new StackAsMyArrayList<Character>();
        bottles[2] = new StackAsMyArrayList<Character>();
        bottles[3] = new StackAsMyArrayList<Character>();
        bottles[4] = new StackAsMyArrayList<Character>();

		// -------------------------------------------------------------------------------------------------------
		// create temp array to see how many colors stack in the bottle
		StackAsMyArrayList temp[] = new StackAsMyArrayList[1];
		temp[0] = new StackAsMyArrayList<Character>();
		// -------------------------------------------------------------------------------------------------------

		int moves = 0;// number of moves to mix the water
		int source = 0; // number of bottle to pour FROM
		int target = 0; // number of bottle to pour TO
		int max = 4; // total number of items allowed per bottle
		Random randomNum = new Random();

		//////STRATEGY #1
		while (moves<4) // 4 moves per 3 colors = 12 moves required
        {
			// get source bottle
			target = randomNum.nextInt(max+1);
			while (bottles[target].getStackSize() == 4)// target is full
			{
				target = randomNum.nextInt(max);
			}
          	bottles[target].push(blue);
		  	target = randomNum.nextInt(max+1);
		 	 while (bottles[target].getStackSize() == 4)// target is full
			{
               target = randomNum.nextInt(max);
			}
          	bottles[target].push(red);
		  	target = randomNum.nextInt(max+1);
		  	while (bottles[target].getStackSize() == 4)// target is full
			{
               target = randomNum.nextInt(max);
			}
          	bottles[target].push(green);
          	// increment valid moves
          	moves++;
        }

		// -------------------------------------------------------------------------------------------------------
		// call method after bottles have been populated and start receiving user commands
		input(bottles, temp[0]);
		// -------------------------------------------------------------------------------------------------------
    }

	// -------------------------------------------------------------------------------------------------------
	// method for user input
	public static void input(StackAsMyArrayList<Character> bottles[], StackAsMyArrayList<Character> temp)
	{
		// declare scanner
		Scanner in = new Scanner(System.in);
		// source bottle index
		int source = 0;
		// target bottle index
		int target = 0;
		// amount to pour from bottle
		int numToPour = 0;

		while (!solved(bottles))
		{
			// show all bottles
			showAll(bottles);
			// receiving input for source bottle
			while (true)
			{
				System.out.println("\nSelect a bottle to pour from: (0-4)");
				source = in.nextInt();

				// check user input
				while (source < 0 || source > 4)
				{
					System.out.println("\nEnter only a value between 1-4!\n");
					showAll(bottles);
					System.out.println("Select a bottle to pour from: (0-4)");
					source = in.nextInt();
					
				}

				// if bottle is empty output message
				if (bottles[source].getStackSize() == 0)
				{
					System.out.println("\nSource Bottle is empty!\n");
				}

				// see if source has valid range and if source bottle is not empty
				if (source < 5 || source > -1 && bottles[source].getStackSize() != 0)
				{
					break;
				}	
			}

			// count will be used to reset program if user is stuck
			int count = 0;
			// recieve input for target bottle
			while (true)
			{
				System.out.println("\nPour from Bottle " + source + " to Bottle(0-4)?");
				target = in.nextInt();

				// check user input
				while (target < 0 || target > 4)
				{
					System.out.println("\nEnter only a value between 1-4!\n");
					showAll(bottles);
					System.out.println("Pour from Bottle " + source + " to Bottle(0-4)?");
					target = in.nextInt();

				}

				// if bottle is full output message
				if (bottles[target].getStackSize() == 4)
				{
					System.out.println("\nTarget Bottle is full!\n");
					input(bottles, temp);
				}

				// if while loop exceeds 2 then user will be promted to reselect a source bottle
				if (count > 2)
				{
					System.out.println("Would you like to reselect a source bottle? YES = 1; NO = 2: ");
					int response = in.nextInt();
					
					if (response == 1)
					{
						input(bottles, temp);
					}
				}

				// test whether user can do the operation with all matching colors below be poured with
				int tmp = getNumberOfFollowingColors(bottles[source], temp);
				int tmp1 = 4 - bottles[target].getStackSize();
				if (tmp > tmp1)
				{
					System.out.println("\nBottle " + target + " cannot contain the contents of Bottle " + source + "!\n");
					input(bottles, temp);
				}
				else 
				{
					numToPour = tmp;
				}

				// see if target has valid range and if target bottle is not full then continue
				if (target < 5 || target > -1 && bottles[target].getStackSize() != 4)
				{
					break;
				}	

				count++;
			}

			// if colors do not match reload program
			if (bottles[source].peek() != bottles[target].peek() && bottles[target].getStackSize() != 0)
			{
				System.out.println("\nThe colors at the top of both bottles do not match!\n");
				input(bottles, temp);
			}

			// do the user operation (numToPour) many times
			for (int i = 0; i < numToPour; i++)
			{
				bottles[target].push(bottles[source].pop());
			}

			// check if game is solved and output message
			if (solved(bottles))
			{
				System.out.println("\n\n\n---------------------------------------------------------");
				showAll(bottles);
				System.out.println("---------------------------------------------------------");
				System.out.println("\nCongratulations! You passed this round. Reload the program for another.");
				System.exit(0);
			}

		}
		in.close();

	}
	// -------------------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------
	// method to see if game is solved
	public static boolean solved(StackAsMyArrayList<Character> bottles[])
	{
		// iterate through bottles and return false if bottles has ink of 1-3
		for (int i = 0; i < 5; i++)
		{
			if (bottles[i].getStackSize() != 0 && bottles[i].getStackSize() < 4)
			{
				return false;
			}
		}

		// if all bottles pass the uniform check method return true 
		if (bottles[0].checkStackUniform() == true && bottles[1].checkStackUniform() == true && bottles[2].checkStackUniform() == true && bottles[3].checkStackUniform() == true && bottles[4].checkStackUniform() == true)
		{
			return true;	
		}
		return false;
	}
	// -------------------------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------------------------
	// method to see how many of the same color should be poured over
	public static int getNumberOfFollowingColors(StackAsMyArrayList<Character> bottle, StackAsMyArrayList<Character> temp)
	{	
		// total of same colors 
		int total = 1;
		// total of colors taken from stack
		int numOperations = 0;

		for(int i = 0; i < bottle.getStackSize(); i++)
		{
			// fill temp and see if there is more of the same color in bottle
			temp.push(bottle.pop());
			numOperations++;
			if(temp.peek() == bottle.peek())
			{
				total++;
			}
			// stop if the top color of bottle doesn't match temp
			else
			{
				break;
			}
			
		}

		// push colors back from temp array, (numOperations) amount of times
		for(int i = 0; i < numOperations; i++)
		{
			bottle.push(temp.pop());
		}

		return total;
	}
	// -------------------------------------------------------------------------------------------------------
}