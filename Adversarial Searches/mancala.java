import java.io.*;
import java.util.*;
public class mancala{
	static boolean enableDebug=false;
	
	/* Print utility method */
	public static void print(String outString){
		if(enableDebug){
			System.out.print(outString);
			System.out.println();
		}
	}
	
	/* Method to getInput from input file and store in an ArrayList data structure. */
	public static ArrayList<String> getInput(String[] args) throws IOException{
		String fileName = args[1];
		FileReader f = new FileReader(fileName);
		BufferedReader br = new BufferedReader(f);
		String line;
		ArrayList<String> list = new ArrayList<String>();
		while((line=br.readLine())!=null){
			list.add(line);
		}
		return list;
	}
	
	public static void main(String[] args) throws IOException{
		File file= new File("traverse_log.txt");
		File nextStateFile = new File("next_state.txt");
		if(!nextStateFile.exists())
			nextStateFile.createNewFile();
		if(!file.exists())
			file.createNewFile();
		PrintWriter writer = new PrintWriter(file,"UTF-8");
		PrintWriter nextStateWriter = new PrintWriter(nextStateFile,"UTF-8");
		ArrayList<String> inputList = mancala.getInput(args);
		for(String x:inputList)
			print(x);
		int task = Integer.parseInt(inputList.get(0));
		if(task==2){
			writer.println("Node,Depth,Value");
		}
		else if(task==3){
			writer.println("Node,Depth,Value,Alpha,Beta");
		}
		print("task is "+inputList.get(0));
		if(task==1){
			int greedyp = Integer.parseInt(inputList.get(1));
			ArrayList<Integer[]> greedyOut = new ArrayList<Integer[]>();
			greedyOut = mancala.greedy(inputList);
			if(greedyp==1){
				for(int j:greedyOut.get(2))
					nextStateWriter.print(j+" ");
				nextStateWriter.println();
				for(int k:greedyOut.get(1))
					nextStateWriter.print(k+" ");
				nextStateWriter.println();
				nextStateWriter.println(greedyOut.get(3)[1]);
				nextStateWriter.println(greedyOut.get(3)[0]);
			}
			else{
				for(int j=0;j<greedyOut.get(1).length;j++)
					nextStateWriter.print(greedyOut.get(2)[greedyOut.get(2).length-j-1]+" ");
				nextStateWriter.println();
				for(int k=0;k<greedyOut.get(1).length;k++)
					nextStateWriter.print(greedyOut.get(1)[greedyOut.get(1).length-k-1]+" ");
				nextStateWriter.println();
				nextStateWriter.println(greedyOut.get(3)[1]);
				nextStateWriter.println(greedyOut.get(3)[0]);
			}
			
			
		}
		else if(task==2){
			mancala.minimax(inputList);
			int p = 0;
			p = nextState.get(0)[0];
			if(p==1){
				for(int j:nextState.get(2))
					nextStateWriter.print(j+" ");
				nextStateWriter.println();
				for(int k:nextState.get(1))
					nextStateWriter.print(k+" ");
				nextStateWriter.println();
				nextStateWriter.println(nextState.get(4)[0]);
				nextStateWriter.println(nextState.get(3)[0]);
			}
			else{
				for(int j=0;j<nextState.get(1).length;j++)
					nextStateWriter.print(nextState.get(2)[nextState.get(2).length-j-1]+" ");
				nextStateWriter.println();
				for(int k=0;k<nextState.get(1).length;k++)
					nextStateWriter.print(nextState.get(1)[nextState.get(1).length-k-1]+" ");
				nextStateWriter.println();
				nextStateWriter.println(nextState.get(4)[0]);
				nextStateWriter.println(nextState.get(3)[0]);
			}
			
			for(traverseLog t:tl){
				writer.println(t.outputLog());
				
			}
			tl.clear();
		}
		else if(task==3){
			mancala.pruning(inputList);
			int p = 0;
			p = nextStateAB.get(0)[0];
			if(p==1){
				for(int j:nextStateAB.get(2))
					nextStateWriter.print(j+" ");
				nextStateWriter.println();
				for(int k:nextStateAB.get(1))
					nextStateWriter.print(k+" ");
				nextStateWriter.println();
				nextStateWriter.println(nextStateAB.get(4)[0]);
				nextStateWriter.println(nextStateAB.get(3)[0]);
			}
			else{
				for(int j=0;j<nextStateAB.get(1).length;j++)
					nextStateWriter.print(nextStateAB.get(2)[nextStateAB.get(2).length-j-1]+" ");
				nextStateWriter.println();
				for(int k=0;k<nextStateAB.get(1).length;k++)
					nextStateWriter.print(nextStateAB.get(1)[nextStateAB.get(1).length-k-1]+" ");
				nextStateWriter.println();
				nextStateWriter.println(nextStateAB.get(4)[0]);
				nextStateWriter.println(nextStateAB.get(3)[0]);
			}
			
			for(traverseLog t:tlg){
				writer.println(t.outputLogAB());
				
			}
			tlg.clear();
			
		}
		nextStateWriter.close();
		writer.close();	
	}
	
	public static ArrayList<Integer[]> greedy(ArrayList<String> inputList){
		ArrayList<Integer[]> printOut = new ArrayList<Integer[]>();
		int player = Integer.parseInt(inputList.get(1));
		String[] playerTwoBoard = inputList.get(3).split("\\s+");			
		String[] playerOneBoard = inputList.get(4).split("\\s+");
		Integer[] boardOne = new Integer[playerOneBoard.length];
		Integer[] boardTwo = new Integer[playerTwoBoard.length];
		if(player==1){
			for(int i=0;i<playerOneBoard.length;i++){
				boardOne[i] = Integer.parseInt(playerOneBoard[i]);
				boardTwo[i] = Integer.parseInt(playerTwoBoard[i]);
			}
		}
		else{
			for(int i=0;i<playerOneBoard.length;i++){
				boardOne[playerOneBoard.length-i-1] = Integer.parseInt(playerOneBoard[i]);
				boardTwo[playerOneBoard.length-i-1] = Integer.parseInt(playerTwoBoard[i]);
			}
		}
		ArrayList<Integer[]> originalBoard = new ArrayList<Integer[]>();
		originalBoard.add(boardOne);
		originalBoard.add(boardTwo);
		int mancalaTwo = Integer.parseInt(inputList.get(5));
		int mancalaOne = Integer.parseInt(inputList.get(6));
		ArrayList<Integer> originalMancala = new ArrayList<Integer>();
		originalMancala.add(mancalaOne);
		originalMancala.add(mancalaTwo);
		int boardSize = playerOneBoard.length;
		printOut = getNextState(boardSize,player,originalBoard,originalMancala);
		if(enableDebug)System.out.println("BEST STATE IS:");
		if(enableDebug)System.out.println();
		if(player==1){
			for(int i=0;i<printOut.get(2).length;i++){
				if(enableDebug)System.out.print(printOut.get(2)[i]+" ");
			}
			if(enableDebug)System.out.println();
			for(int i=0;i<printOut.get(1).length;i++){
				if(enableDebug)System.out.print(printOut.get(1)[i]+" ");
			}
			if(enableDebug)System.out.println();
			if(enableDebug)System.out.println(printOut.get(3)[1]);
			if(enableDebug)System.out.println(printOut.get(3)[0]);
		}
		else{
			for(int i = printOut.get(2).length-1;i>=0;i--){
				if(enableDebug)System.out.print(printOut.get(2)[i]+" ");
			}
			if(enableDebug)System.out.println();
			for(int i=printOut.get(1).length-1;i>=0;i--){
				if(enableDebug)System.out.print(printOut.get(1)[i]+" ");
			}
			if(enableDebug)System.out.println();
			if(enableDebug)System.out.println(printOut.get(3)[1]);
			if(enableDebug)System.out.println(printOut.get(3)[0]);
			
		}
		
		originalBoard.clear();
		originalMancala.clear();
		return printOut;
	}
	
	public static void printBoard(ArrayList<Integer[]> boardp,ArrayList<Integer> mancalap, int player){
		boolean enableDebugBoard = false;
		if(enableDebugBoard)System.out.println("Board");
		if(player==1){
			if(enableDebugBoard)System.out.println();
			for(int j:boardp.get(1))
				if(enableDebugBoard)System.out.print(j+" ");
			if(enableDebugBoard)System.out.println();
			for(int k:boardp.get(0))
				if(enableDebugBoard)System.out.print(k+" ");
			if(enableDebugBoard)System.out.println();
			if(enableDebugBoard)System.out.println(mancalap.get(1));
			if(enableDebugBoard)System.out.println(mancalap.get(0));
		}
		else{
			if(enableDebugBoard)System.out.println();
			for(int j=0;j<boardp.get(1).length;j++)
				if(enableDebugBoard)System.out.print(boardp.get(1)[boardp.get(1).length-j-1]+" ");
			if(enableDebugBoard)System.out.println();
			for(int k=0;k<boardp.get(0).length;k++)
				if(enableDebugBoard)System.out.print(boardp.get(0)[boardp.get(1).length-k-1]+" ");
			if(enableDebugBoard)System.out.println();
			if(enableDebugBoard)System.out.println(mancalap.get(1));
			if(enableDebugBoard)System.out.println(mancalap.get(0));
		}
	}
	
	public static boolean isUnstableState(Integer[] board){
		boolean val = true;
		for(int i:board){
			if(i!=0){
				val = false;
			}
		}
		return val;
	}

	public static ArrayList<Integer[]> getNextState(int boardSize, int player, ArrayList<Integer[]> originalBoard, ArrayList<Integer> originalMancala){
			
			HashMap<String, Integer> evalFunction = new HashMap<String, Integer>();
			HashMap<String , ArrayList<Integer[]>> returnBestBoard = new HashMap<String, ArrayList<Integer[]>>();
			class evalFunctionComparator implements Comparator<String>{
				public int compare(String a,String b){
					if(evalFunction.get(a)<evalFunction.get(b))
						return 1;
					else if(evalFunction.get(a)>evalFunction.get(b))
						return -1;
					else
						return a.compareTo(b);
				}
	
			}
			evalFunction.clear();
			Comparator<String> comp = new evalFunctionComparator();
			PriorityQueue<String> frontier = new PriorityQueue<String>(comp);
			frontier.clear();
			//Stack<String> path = new Stack<String>();
			ArrayList<Integer> traversalOrder = new ArrayList<Integer>();
			
			if(player == 1){
					for(Integer t=0;t<boardSize;t++){
						traversalOrder.add(t);
					}
			}
			else{
				for(Integer t=boardSize-1;t>=0;t--){
					traversalOrder.add(t);
				}
			}
		for(Integer j:traversalOrder){
			ArrayList<Integer[]> boardConfig = new ArrayList<Integer[]>();
			boolean exceptionalCase = false;
			int evalFunctionCaseThreeRec = 0;
			int evalFunctionValue = 0;
			if(enableDebug)System.out.println("Original: ");
			printBoard(originalBoard,originalMancala,player);
			//select each pit on the board and calculate next moves.once we get the next moves we will find the evaluation function and add in arraylist
			int selectedPit=j;
			if(enableDebug)System.out.println("selected pit is:"+j);
			//initalize the array which will store new board for current player after a move has been made 
			Integer[] playerOpponent=new Integer[boardSize];
			int opponent=0;
			if(player==1){
				opponent = 2;
			}
			else{
				opponent = 1;
			}
			for(int i = 0;i<boardSize;i++){
				playerOpponent[i] = originalBoard.get(opponent-1)[i];
			}
			Integer[] playerCurrent = new Integer[boardSize];
			for(int i = 0;i<boardSize;i++){
				playerCurrent[i] = originalBoard.get(player-1)[i];
			}
				
			boolean flag=true;
			int count = playerCurrent[selectedPit];
		//print("Number of pits is:"+Integer.toString(boardSize));
			while(flag){
				if(enableDebug)System.out.println("Count is:"+count);
				if(count==0){
					flag=false;
					print("try another state");
				}
				else{
					if(count<(boardSize-selectedPit)){
						//also include the case when the last stone ends in empty pit on my side//
						int next = 1;
						print("From Case 1:");
						playerCurrent[selectedPit]=0;
						int numberOfStones=0;
						while(next<=count){
							//if the last stone is being placed on an empty pit then remove that stone and all the stones in the opposite pit and add to mancala
							if(next==count && playerCurrent[selectedPit+next]==0){
								numberOfStones = playerOpponent[selectedPit+next]+1;
								playerOpponent[selectedPit+next]=0;
								next=next+1;
							}
							else{
								playerCurrent[selectedPit+next] = playerCurrent[selectedPit+next] + 1;
								next=next+1;
							}
						}
						//NEW CODE *********************************************************** NEW CODE//
						int opponentMancala = 0;
						if(isUnstableState(playerCurrent)){
							for(int z=0; z<boardSize;z++){
								opponentMancala = opponentMancala + playerOpponent[z];
								playerOpponent[z] = 0;
							}
						}
						int currentMancala = 0;
						if(isUnstableState(playerOpponent)){
							for(int z=0; z<boardSize;z++){
								currentMancala = currentMancala + playerCurrent[z];
								playerCurrent[z] = 0;
							}
						}
						//NEW CODE *********************************************************** NEW CODE//
						ArrayList<Integer[]> newBoard = new ArrayList<Integer[]>();
						ArrayList<Integer> newMancala = new ArrayList<Integer>();
						if(player==1){
							newBoard.add(playerCurrent);
							newBoard.add(playerOpponent);	
							newMancala.add(originalMancala.get(player-1)+numberOfStones+currentMancala);
							newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
						}
						else{
							newBoard.add(playerOpponent);
							newBoard.add(playerCurrent);
							newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
							newMancala.add(originalMancala.get(player-1)+numberOfStones + currentMancala);			
						}
						printBoard(newBoard,newMancala,player);
						evalFunctionValue = newMancala.get(player-1) - newMancala.get(opponent-1);
						if(enableDebug)System.out.println("Eval value is:"+evalFunctionValue+"for pit:"+j);
						String s = "";
						if(player==1){
							s="b"+j;
						}
						else{
							int k = boardSize - j -1;
							s = "a"+k;
						}
						evalFunction.put(s,evalFunctionValue);
						Integer[] newEvalFuncArr = new Integer[1];
						newEvalFuncArr[0] = evalFunctionValue;
						Integer[] newMancalaArr = new Integer[2];
						newMancalaArr = newMancala.toArray(newMancalaArr);
						//newEvalFuncArr = evalFunctionValue.toArray(newEvalFuncArr);
						boardConfig.add(newEvalFuncArr);
						if(player==1){
							boardConfig.add(playerCurrent);	
							boardConfig.add(playerOpponent);
						}
						else{
							boardConfig.add(playerOpponent);
							boardConfig.add(playerCurrent);	
						}
						boardConfig.add(newMancalaArr);
						returnBestBoard.put(s,boardConfig);
						frontier.add(s);
						flag=false;
						newBoard.clear();
						
					}	
					else if(count==(boardSize-selectedPit)){
						ArrayList<Integer[]> temp = new ArrayList<Integer[]>();
						boardConfig.clear();
						//Last stone will end in Mancala so ...one more chance...calculate eval function for the remaining pits..select pit which is not empty..
						print("From case 2:");
						int next = 1;
						playerCurrent[selectedPit]=0;
						while(next<count){
							playerCurrent[selectedPit+next] = playerCurrent[selectedPit+next] + 1;
							next=next+1;
						}
						//NEW CODE ***************************************************************** NEW CODE//
						boolean doRecursion = true;
						int opponentMancala = 0;
						if(isUnstableState(playerCurrent)){
							doRecursion = false;
							for(int i=0;i<boardSize;i++){
								opponentMancala = opponentMancala + playerOpponent[i];
								playerOpponent[i] = 0;
							}
						}
						
						int currentMancala = 0;
						if(isUnstableState(playerOpponent)){
							doRecursion = false;
							for(int i=0;i<boardSize;i++){
								currentMancala = currentMancala + playerCurrent[i];
								playerOpponent[i] = 0;
							}
						}
						//NEW CODE ***************************************************************** NEW CODE//
						ArrayList<Integer[]> newBoard = new ArrayList<Integer[]>();
						ArrayList<Integer> newMancala = new ArrayList<Integer>();
						
						if(player==1){
							newBoard.add(playerCurrent);
							newBoard.add(playerOpponent);
							newMancala.add(originalMancala.get(player-1)+1+currentMancala);
							newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
						}
						else{
							newBoard.add(playerOpponent);
							newBoard.add(playerCurrent);
							newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
							newMancala.add(originalMancala.get(player-1)+1+currentMancala);
						}
						printBoard(newBoard,newMancala,player);
						String s = "";
							if(player==1){
								s="b"+j;
								if(enableDebug)System.out.println(s);
							}
							else{
								int k = boardSize - j -1;
								s = "a"+k;
							}
						//NEW CODE ***************************************************************** NEW CODE//
						if(doRecursion){
							temp = getNextState(boardSize,player,newBoard,newMancala);
						
						//make this getNextState return arraylist with first element as the next pit selected and second value as the evalfunction value of that pit
						//evalFunctionValue = newMancala.get(0) - newMancala.get(1);
							evalFunctionValue = temp.get(0)[0];
							if(enableDebug)System.out.println("Eval value is:"+evalFunctionValue+" for pit:"+j);
							evalFunction.put(s,evalFunctionValue);
							if(enableDebug)System.out.println("Size of boardConfig:"+temp.size());
							boardConfig.add(temp.get(0));
							boardConfig.add(temp.get(1));
							boardConfig.add(temp.get(2));
							boardConfig.add(temp.get(3));
						}
						
						else{
							evalFunctionValue = newMancala.get(player-1) - newMancala.get(opponent-1);
							if(enableDebug)System.out.println("Eval value is:"+evalFunctionValue+" for pit:"+j);
							if(enableDebug)System.out.println(s);
							evalFunction.put(s,evalFunctionValue);
							Integer[] evalFunctionArr = new Integer[1];
							evalFunctionArr[0] = evalFunctionValue;
							boardConfig.add(evalFunctionArr);
							if(player == 1){
								boardConfig.add(playerCurrent);
								boardConfig.add(playerOpponent);
								Integer[] mancalaArr = new Integer[2];
								mancalaArr = newMancala.toArray(mancalaArr);
								boardConfig.add(mancalaArr);
							}
							else{
								boardConfig.add(playerOpponent);
								boardConfig.add(playerCurrent);
								Integer[] mancalaArr = new Integer[2];
								mancalaArr = newMancala.toArray(mancalaArr);	
								boardConfig.add(mancalaArr);
							}
						//NEW CODE ***************************************************************** NEW CODE//	
						}
						returnBestBoard.put(s,boardConfig);
						frontier.add(s);
						flag=false;
					}
					else if(count>(boardSize-selectedPit)){
						ArrayList<Integer[]> temp = new ArrayList<Integer[]>();
						print("From Case 3");
						int remaining = count;
						int next = 1;
						int currentStones = originalMancala.get(player-1);
						playerCurrent[selectedPit]=0;
						boolean curr= true;
						boolean opp = false;
						int prev = 0;
						int currentMancala = 0;
						int opponentMancala = 0;
						boolean expcase = false;
						while(remaining>0){
							if(curr){
								if(selectedPit+next==boardSize){
									if(remaining-1==0){
										exceptionalCase = true;
										//if this is the7 last stone.. update the board and call recursive func
										int currentStonesR = currentStones + 1;
										if(isUnstableState(playerCurrent)){
											exceptionalCase = false;
											expcase = true;
											for(int i = 0;i<boardSize;i++){
												opponentMancala = opponentMancala + playerOpponent[i];
												playerOpponent[i] = 0;
											}
										}
										if(isUnstableState(playerOpponent)){
											exceptionalCase = false;
											expcase = true;
											for(int i =0 ;i<boardSize;i++){
												currentMancala = currentMancala + playerCurrent[i];
												playerCurrent[i]=0;
											}
											//currentMancala = currentMancala + 1 ;
										}
										//Check for unstable state.. if any state playerCurrent or playerOpponent is all zeros then return value and do not recurse
										if(exceptionalCase){
											ArrayList<Integer> newMancalaR = new ArrayList<Integer>();
											ArrayList<Integer[]> newBoardR = new ArrayList<Integer[]>();
											if(player==1){
												newBoardR.add(playerCurrent);
												newBoardR.add(playerOpponent);
												newMancalaR.add(currentStonesR);
												newMancalaR.add(originalMancala.get(opponent-1));
											}
											else{
												newBoardR.add(playerOpponent);
												newBoardR.add(playerCurrent);
												newMancalaR.add(originalMancala.get(opponent-1));
												newMancalaR.add(currentStonesR);
											}
											temp = getNextState(boardSize,player,newBoardR,newMancalaR);
											evalFunctionCaseThreeRec = temp.get(0)[0];
										}
										//evalFunctionValue = newMancala.get(0) - newMancala.get(1);
										//if(enableDebug)System.out.println("Eval value is:"+evalFunctionValue);
										
									}
									currentStones = currentStones + 1 +currentMancala;
									remaining=remaining -1;
									
									if(remaining>0){
										opp = true;
										prev = boardSize -1;
									}
									curr = false;
								}
								else if(selectedPit+next<boardSize){
									if(remaining-1==0 && playerCurrent[selectedPit+next]==0){
										currentStones = currentStones + playerOpponent[selectedPit+next]+1;
										playerOpponent[selectedPit+next]=0;
										remaining = remaining - 1;
										next = next+1;
									}
									else{
										playerCurrent[selectedPit+next] = playerCurrent[selectedPit+next] + 1;
										remaining = remaining-1;
										next=next+1;
									}
								}
							}
							else if(opp){
								if(prev>=0){
									playerOpponent[prev] = playerOpponent[prev]+1;
									remaining = remaining-1;
									prev = prev - 1;
								}
								else{
									if(remaining>0){
										curr= true;
										next = 0;
										selectedPit = 0;
									}
								}
							}
						}
				
						if(!expcase){
							if(isUnstableState(playerCurrent)){
								for(int i = 0;i<boardSize;i++){
									opponentMancala = opponentMancala+playerOpponent[i];
									playerOpponent[i] = 0;
								}
							}
							if(isUnstableState(playerOpponent)){
								for(int i=0;i<boardSize;i++){
									currentMancala = currentMancala+playerCurrent[i];
									playerCurrent[i]=0;
								}
								currentStones = currentStones + currentMancala;
							}
						}
						ArrayList<Integer[]> newBoard = new ArrayList<Integer[]>();
						ArrayList<Integer> newMancala = new ArrayList<Integer>();
						if(player==1){
							newBoard.add(playerCurrent);
							newBoard.add(playerOpponent);
							newMancala.add(currentStones);
							newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
						}
						else{
							newBoard.add(playerOpponent);
							newBoard.add(playerCurrent);
							newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
							newMancala.add(currentStones);
						}
						printBoard(newBoard,newMancala,player);
						String s = "";
						if(player==1){
							s="b"+j;
						}
						else{
							int k = boardSize - j -1;
							s = "a"+k;
						}
						
						if(exceptionalCase){
							evalFunctionValue = evalFunctionCaseThreeRec;
							boardConfig.add(temp.get(0));
							boardConfig.add(temp.get(1));
							boardConfig.add(temp.get(2));
							boardConfig.add(temp.get(3));
							returnBestBoard.put(s,boardConfig);
						}
						else{
							evalFunctionValue = newMancala.get(player-1) - newMancala.get(opponent-1);
							Integer[] newEvalFuncArr = new Integer[1];
							newEvalFuncArr[0] = evalFunctionValue;
							//newEvalFuncArr = evalFunctionValue.toArray(newEvalFuncArr);
							boardConfig.add(newEvalFuncArr);
							if(player == 1){
								boardConfig.add(playerCurrent);
								boardConfig.add(playerOpponent);
							}
							else{
								boardConfig.add(playerOpponent);
								boardConfig.add(playerCurrent);
							}
							Integer[] newMancalaArr = new Integer[2];
							newMancalaArr = newMancala.toArray(newMancalaArr);
							boardConfig.add(newMancalaArr);
							returnBestBoard.put(s,boardConfig);
						}
						if(enableDebug)System.out.println("eval Function is:"+evalFunctionValue+" for pit:"+j);
						
						evalFunction.put(s,evalFunctionValue);
						frontier.add(s);
						flag=false;
					}
					
				}
			}
			
			
		}
		String bestMove = frontier.poll();
		//path.push(bestMove);
		if(enableDebug)System.out.println("best possible val:"+bestMove);
		int bestEval = evalFunction.get(bestMove);
		if(enableDebug)System.out.println("best utility:"+bestEval);
		//String nextBest = frontier.poll();
		//if(enableDebug)System.out.println("next best:"+nextBest);
		//int nextBestVal = evalFunction.get(nextBest);
		//if(enableDebug)System.out.println("next best util:"+nextBestVal);
		String str="";
		//while(!(path.empty())){
			
		//	if(enableDebug)System.out.println("HELLO"+path.pop());
		//}
		if(enableDebug)System.out.println(returnBestBoard.get(bestMove).get(1)[0]);
		
		return returnBestBoard.get(bestMove);
		
	}
	public static void minimax(ArrayList<String> inputList){
		print("Hello from minimax");
		int player = Integer.parseInt(inputList.get(1));
		int maxDepth = Integer.parseInt(inputList.get(2));
		String[] playerTwoBoard = inputList.get(3).split("\\s+");			
		String[] playerOneBoard = inputList.get(4).split("\\s+");
		Integer[] boardOne = new Integer[playerOneBoard.length];
		Integer[] boardTwo = new Integer[playerTwoBoard.length];
		int boardSize = playerOneBoard.length;
		int mancalaTwo = Integer.parseInt(inputList.get(5));
		int mancalaOne = Integer.parseInt(inputList.get(6));
		ArrayList<Integer> originalMancala = new ArrayList<Integer>();
		originalMancala.add(mancalaOne);
		originalMancala.add(mancalaTwo);
		for(int i=0;i<playerOneBoard.length;i++){
			boardOne[i] = Integer.parseInt(playerOneBoard[i]);
			boardTwo[i] = Integer.parseInt(playerTwoBoard[i]);
		}
		ArrayList<Integer[]> originalBoard = new ArrayList<Integer[]>();
		originalBoard.add(boardOne);
		originalBoard.add(boardTwo);
		int currentDepth = 0;
		int maxPlayer = player;
		String pitName = "root";
		int bestVal = minimaxDecision(player, maxPlayer,originalBoard, originalMancala,boardSize,maxDepth,currentDepth,pitName,false);
		if(enableDebug)System.out.println("Best Val is:"+bestVal);
		//for(traverseLog t:tl){
		//	t.printLog();
		//}

	}
	
	public static ArrayList<Integer> getTraversalOrder(int player, int boardSize){
		ArrayList<Integer> tO = new ArrayList<Integer>();
		tO.clear();
		if(player ==1){
			for(Integer i=0;i<boardSize;i++)
				tO.add(i);
		}
		else{
			for(Integer i =boardSize-1;i>=0;i++)
				tO.add(i);
		}
		return tO;
		
	}
	public static ArrayList<traverseLog> tl = new ArrayList<traverseLog>();
	public static ArrayList<Integer[]> nextState = new ArrayList<Integer[]>();
	public static int uniScore = (int)Double.NEGATIVE_INFINITY;
	public static int minimaxDecision(int player, int maxPlayer,ArrayList<Integer[]> originalBoard, ArrayList<Integer> originalMancala, int boardSize, int maxDepth, int currentDepth, String pitName, boolean rec){
		int score = 0;
		
		if(gameOver(originalBoard)){
			print("Game Over!");
			//printBoard(originalBoard,originalMancala,player);
			score = calculateUtility(originalMancala,maxPlayer);
			if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+","+score+",");
			traverseLog x= new traverseLog(pitName,currentDepth,score);
			tl.add(x);
		}
		else if(terminalTest(currentDepth,maxDepth) && (!rec)){
			print("Terminal Test and previous call not recursive!");
			score = calculateUtility(originalMancala,maxPlayer);
			if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+","+score+",");
			traverseLog x= new traverseLog(pitName,currentDepth,score);
			tl.add(x);
		}
		else{
			int bestScore = 0;
			//ArrayList<Integer[]> bestState = new ArrayList<Integer[]>();
			int bestMancalaOne=0;
			int bestMancalaTwo=0;
			if(!rec){
				if(player == maxPlayer){
					bestScore = (int)Double.NEGATIVE_INFINITY;
					//Enter NodeName, depth, value to traverseLog
					//print("Eval value:"+bestScore);
					if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+",-Infinity");
					traverseLog x= new traverseLog(pitName,currentDepth,bestScore);
					tl.add(x);
				}
				else{
					bestScore = (int)Double.POSITIVE_INFINITY;
					//Enter node name, depth, value to traverseLog
					//print("Eval value:"+bestScore);
					if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+",Infinity");
					traverseLog x= new traverseLog(pitName,currentDepth,bestScore);
					tl.add(x);
				}
			}
			else{
				if(currentDepth==maxDepth){
					if(player == maxPlayer){
						bestScore = (int)Double.NEGATIVE_INFINITY;
						if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+",-Infinity");
						traverseLog x= new traverseLog(pitName,currentDepth,bestScore);
						tl.add(x);
					}
					else{
						bestScore = (int)Double.POSITIVE_INFINITY;
						if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+",Infinity");
						traverseLog x= new traverseLog(pitName,currentDepth,bestScore);
						tl.add(x);
					}
				}
				else{
					if(player == maxPlayer){
						bestScore = (int)Double.NEGATIVE_INFINITY;
						if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+",-Infinity");
						traverseLog x= new traverseLog(pitName,currentDepth,bestScore);
						tl.add(x);
					}
					else{
						bestScore = (int)Double.POSITIVE_INFINITY;
						if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+",Infinity");
						traverseLog x= new traverseLog(pitName,currentDepth,bestScore);
						tl.add(x);
					}
				}
				//Enter node name, depth, value to traverseLog
				print("Eval value:"+bestScore);
			}
			if(enableDebug)System.out.println("get traversal order for player:"+player);
			ArrayList<Integer> traversalOrder = new ArrayList<Integer>();
			
			if(player == 1){
					for(Integer t=0;t<boardSize;t++){
						traversalOrder.add(t);
					}
			}
			else{
				for(Integer t=boardSize-1;t>=0;t--){
					traversalOrder.add(t);
				}
			}
			if(enableDebug)System.out.println("got traversal order:");
			int recursiveScore = 0;
			int opponent= 0;
			for(int j:traversalOrder){
				int selectedPit = j;
				if(enableDebug)System.out.println("DEPTH"+currentDepth);
				//if(enableDebug)System.out.println("Original: ");
				//printBoard(originalBoard,originalMancala,player);
				Integer[] playerCurrent = new Integer[boardSize];
				Integer[] playerOpponent=new Integer[boardSize];
				String s = "";
				//int opponent=0;
				//this is done when recursion is not true
				if(pitName.compareTo("root")==0){
					if(player==1){
						int k = j + 2;
						s="B"+k;
						for(int i=0;i<boardSize;i++){
							playerCurrent[i] = originalBoard.get(0)[i];
							playerOpponent[i] = originalBoard.get(1)[i];
						}
						opponent = 2;
					}
					else{
						int k = boardSize - j -1+2;
						s = "A"+k;
						for(int i=0;i<boardSize;i++){
							playerCurrent[boardSize-i-1] = originalBoard.get(1)[i];
							playerOpponent[boardSize-i-1] = originalBoard.get(0)[i];
						}
						opponent = 1;
						
					}

				}
				else{
					if(player==1 && rec){
						int k = j + 2;
						s="B"+k;
						for(int i=0;i<boardSize;i++){
							playerCurrent[i] = originalBoard.get(0)[i];
							playerOpponent[i] = originalBoard.get(1)[i];
						}
						opponent = 2;
					}
					else if(player==1 && !rec){
						int k = j + 2;
						s="B"+k;
						for(int i=0;i<boardSize;i++){
							playerCurrent[boardSize-i-1] = originalBoard.get(0)[i];
							playerOpponent[boardSize-i-1] = originalBoard.get(1)[i];
						}
						opponent = 2;
						
					}



					else if(player==2 && rec){
						int k = boardSize - j -1+2;
						s = "A"+k;
						for(int i=0;i<boardSize;i++){
							playerCurrent[i] = originalBoard.get(1)[i];
							playerOpponent[i] = originalBoard.get(0)[i];
						}
						opponent = 1;
					}


					else if(player==2 && !rec){
						int k = boardSize - j -1+2;
						s = "A"+k;
						for(int i=0;i<boardSize;i++){
							playerCurrent[boardSize-i-1] = originalBoard.get(1)[i];
							playerOpponent[boardSize-i-1] = originalBoard.get(0)[i];
						}
						opponent = 1;
					}

				}
				print("Selected pit:"+s);
				boolean flag=true;
				int count = playerCurrent[selectedPit];
				while(flag){
					if(count==0){
						flag = false;
						print("Try another state");
					}
					else{
						if(count<(boardSize-selectedPit)){
							boolean doRecursion = false;
							int next = 1;
							print("From Case 1:");
							playerCurrent[selectedPit]=0;
							int numberOfStones=0;
							while(next<=count){
								//if the last stone is being placed on an empty pit then remove that stone and all the stones in the opposite pit and add to mancala
								if(next==count && playerCurrent[selectedPit+next]==0){
									numberOfStones = playerOpponent[selectedPit+next]+1;
									playerOpponent[selectedPit+next]=0;
									next=next+1;
								}
								else{
									playerCurrent[selectedPit+next] = playerCurrent[selectedPit+next] + 1;
									next=next+1;
								}
							}
							int opponentMancala = 0;
							if(isUnstableState(playerCurrent)){
								for(int z=0; z<boardSize;z++){
									opponentMancala = opponentMancala + playerOpponent[z];
									playerOpponent[z] = 0;
								}
							}
							int currentMancala = 0;
							if(isUnstableState(playerOpponent)){
								for(int z=0; z<boardSize;z++){
									currentMancala = currentMancala + playerCurrent[z];
									playerCurrent[z] = 0;
								}
							}
							ArrayList<Integer[]> newBoard = new ArrayList<Integer[]>();
							ArrayList<Integer> newMancala = new ArrayList<Integer>();
							if(player==1){
								newBoard.add(playerCurrent);
								newBoard.add(playerOpponent);	
								newMancala.add(originalMancala.get(player-1)+numberOfStones+currentMancala);
								newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
							}
							else{
								newBoard.add(playerOpponent);
								newBoard.add(playerCurrent);
								newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
								newMancala.add(originalMancala.get(player-1)+numberOfStones + currentMancala);			
							}
							printBoard(newBoard,newMancala,player);
							if(rec && !doRecursion){
								recursiveScore = minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,doRecursion);
							}
							else if(!rec && !doRecursion){
								recursiveScore = minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion);
							}
							else if(rec && doRecursion){
								recursiveScore = minimaxDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,doRecursion);
							}
							else if(!rec && doRecursion){
								recursiveScore = minimaxDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion);
							}
							//recursiveScore = minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion);
					
							if(player==maxPlayer){
								if(recursiveScore>bestScore){
									bestScore = recursiveScore;
								}
							}
							else{
								if(recursiveScore<bestScore){
									bestScore = recursiveScore;
								}
							}					
							if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+","+bestScore);
							traverseLog x= new traverseLog(pitName,currentDepth,bestScore);
							tl.add(x);
							flag=false;
							newBoard.clear();	
						}
						else if(count==(boardSize-selectedPit)){
							int nextRecursionScore = 0;
							print("From case 2:");
							ArrayList<Integer[]> temp = new ArrayList<Integer[]>();
							int next = 1;
							playerCurrent[selectedPit]=0;
							while(next<count){
								playerCurrent[selectedPit+next] = playerCurrent[selectedPit+next] + 1;
								next=next+1;
							}
							boolean doRecursion = true;
							int opponentMancala = 0;
							if(isUnstableState(playerCurrent)){
								doRecursion = false;
								for(int i=0;i<boardSize;i++){
									opponentMancala = opponentMancala + playerOpponent[i];
									playerOpponent[i] = 0;
								}
							}
							
							int currentMancala = 0;
							if(isUnstableState(playerOpponent)){
								doRecursion = false;
								for(int i=0;i<boardSize;i++){
									currentMancala = currentMancala + playerCurrent[i];
									playerOpponent[i] = 0;
								}
							}
							ArrayList<Integer[]> newBoard = new ArrayList<Integer[]>();
							ArrayList<Integer> newMancala = new ArrayList<Integer>();
							
							if(player==1){
								newBoard.add(playerCurrent);
								newBoard.add(playerOpponent);
								newMancala.add(originalMancala.get(player-1)+1+currentMancala);
								newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
							}
							else{
								newBoard.add(playerOpponent);
								newBoard.add(playerCurrent);
								newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
								newMancala.add(originalMancala.get(player-1)+1+currentMancala);
							}
							printBoard(newBoard,newMancala,player);
							if(enableDebug)System.out.println(" Value of doRecursion"+doRecursion);
							if(enableDebug)System.out.println(" Value of depth"+currentDepth);
							if(rec && !doRecursion){
								recursiveScore = minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,doRecursion);
							}
							else if(!rec && !doRecursion){
								recursiveScore = minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion);
							}
							else if(rec && doRecursion){
								recursiveScore = minimaxDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,doRecursion);
							}
							else if(!rec && doRecursion){
								recursiveScore = minimaxDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion);
							}
							/*if(doRecursion){
								recursiveScore = minimaxDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,doRecursion);
								//minimaxDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,doRecursion);
							}
							else{
								recursiveScore = minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion);
								//minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion);
							}*/
							if(player==maxPlayer){
								if(recursiveScore>bestScore){
									bestScore = recursiveScore;
								}
							}
							else{
								if(recursiveScore<bestScore){
									bestScore = recursiveScore;
								}
							}
							if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+","+bestScore);
							traverseLog x= new traverseLog(pitName,currentDepth,bestScore);
							tl.add(x);
							flag = false;		
						}
						else if(count>(boardSize - selectedPit)){
							if(enableDebug)System.out.println("From case 3");
							if(enableDebug)System.out.println("selected pit:"+j);
							if(enableDebug)System.out.println("boardSize:"+boardSize);
							if(enableDebug)System.out.println("count:"+count);
							ArrayList<Integer[]> temp = new ArrayList<Integer[]>();
							int remaining = count;
							int next = 1;
							int currentStones = originalMancala.get(player-1);
							playerCurrent[selectedPit]=0;
							boolean curr= true;
							boolean opp = false;
							int prev = 0;
							int currentMancala = 0;
							int opponentMancala = 0;
							boolean expcase = false;
							boolean exceptionalCase = false;
							while(remaining>0){
								if(curr){
									if(selectedPit+next==boardSize){
										if(remaining-1==0){
											exceptionalCase = true;
											//if this is the7 last stone.. update the board and call recursive func
											int currentStonesR = currentStones + 1;
											if(isUnstableState(playerCurrent)){
												exceptionalCase = false;
												expcase = true;
												for(int i = 0;i<boardSize;i++){
													opponentMancala = opponentMancala + playerOpponent[i];
													playerOpponent[i] = 0;
												}
											}
											if(isUnstableState(playerOpponent)){
												exceptionalCase = false;
												expcase = true;
												for(int i =0 ;i<boardSize;i++){
													currentMancala = currentMancala + playerCurrent[i];
													playerCurrent[i]=0;
												}
												//currentMancala = currentMancala + 1 ;
											}
											//Check for unstable state.. if any state playerCurrent or playerOpponent is all zeros then return value and do not recurse
											if(exceptionalCase){
												ArrayList<Integer> newMancalaR = new ArrayList<Integer>();
												ArrayList<Integer[]> newBoardR = new ArrayList<Integer[]>();
												if(player==1){
													newBoardR.add(playerCurrent);
													newBoardR.add(playerOpponent);
													newMancalaR.add(currentStonesR);
													newMancalaR.add(originalMancala.get(opponent-1));
												}
												else{
													newBoardR.add(playerOpponent);
													newBoardR.add(playerCurrent);
													newMancalaR.add(originalMancala.get(opponent-1));
													newMancalaR.add(currentStonesR);
												}
												
												if(rec && !exceptionalCase){
													recursiveScore = minimaxDecision(opponent,maxPlayer,newBoardR,newMancalaR,boardSize,maxDepth,currentDepth,s,exceptionalCase);
												}
												else if(!rec && !exceptionalCase){
													recursiveScore = minimaxDecision(opponent,maxPlayer,newBoardR,newMancalaR,boardSize,maxDepth,currentDepth+1,s,exceptionalCase);
												}
												else if(rec && exceptionalCase){
													recursiveScore = minimaxDecision(player,maxPlayer,newBoardR,newMancalaR,boardSize,maxDepth,currentDepth,s,exceptionalCase);
												}
												else if(!rec && exceptionalCase){
													recursiveScore = minimaxDecision(player,maxPlayer,newBoardR,newMancalaR,boardSize,maxDepth,currentDepth+1,s,exceptionalCase);
												}
												//recursiveScore = minimaxDecision(player,maxPlayer,newBoardR,newMancalaR,boardSize,maxDepth,currentDepth,s,exceptionalCase);
												
											}
											//evalFunctionValue = newMancala.get(0) - newMancala.get(1);
											//if(enableDebug)System.out.println("Eval value is:"+evalFunctionValue);
											
										}
										currentStones = currentStones + 1 +currentMancala;
										remaining=remaining -1;
										if(remaining>0){
											opp = true;
											prev = boardSize -1;
										}
										curr = false;
									}
									else if(selectedPit+next<boardSize){
										if(remaining-1==0 && playerCurrent[selectedPit+next]==0){
											currentStones = currentStones + playerOpponent[selectedPit+next]+1;
											playerOpponent[selectedPit+next]=0;
											remaining = remaining - 1;
											next = next+1;
										}
										else{
											playerCurrent[selectedPit+next] = playerCurrent[selectedPit+next] + 1;
											remaining = remaining-1;
											next=next+1;
										}
									}
									
								}
								else if(opp){
									if(prev>=0){
										playerOpponent[prev] = playerOpponent[prev]+1;
										remaining = remaining-1;
										prev = prev - 1;
									}
									else{
										if(remaining>0){
											curr= true;
											next = 0;
											selectedPit = 0;
										}
									}
								}
							}
							if(!expcase){
								if(isUnstableState(playerCurrent)){
									for(int i = 0;i<boardSize;i++){
										opponentMancala = opponentMancala+playerOpponent[i];
										playerOpponent[i] = 0;
									}
								}
								if(isUnstableState(playerOpponent)){
									for(int i=0;i<boardSize;i++){
										currentMancala = currentMancala+playerCurrent[i];
										playerCurrent[i]=0;
									}
									currentStones = currentStones + currentMancala;
								}
							}
							ArrayList<Integer[]> newBoard = new ArrayList<Integer[]>();
							ArrayList<Integer> newMancala = new ArrayList<Integer>();
							if(player==1){
								newBoard.add(playerCurrent);
								newBoard.add(playerOpponent);
								newMancala.add(currentStones);
								newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
							}
							else{
								newBoard.add(playerOpponent);
								newBoard.add(playerCurrent);
								newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
								newMancala.add(currentStones);
							}
							printBoard(newBoard,newMancala,player);
							if(!exceptionalCase){
									if(rec && !exceptionalCase){
										recursiveScore = minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,exceptionalCase);
									}
									else if(!rec && !exceptionalCase){
										recursiveScore = minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,exceptionalCase);
									}
									else if(rec && exceptionalCase){
										recursiveScore = minimaxDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,exceptionalCase);
									}
									else if(!rec && exceptionalCase){
										recursiveScore = minimaxDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,exceptionalCase);
									}
								//minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,exceptionalCase);
								//recursiveScore = minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,exceptionalCase);
							}
							if(player==maxPlayer){
								if(recursiveScore>bestScore){
									bestScore = recursiveScore;
								}
							}
							else{
								if(recursiveScore<bestScore){
									bestScore = recursiveScore;
								}
							}
							if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+","+bestScore);
							traverseLog x= new traverseLog(pitName,currentDepth,bestScore);
							tl.add(x);
							flag = false;
						}		
					}
				}	
			}
			score = bestScore;
		}
		
		if(currentDepth==1 && score>uniScore){
			nextState.clear();
			if(enableDebug)System.out.println("UNI:"+uniScore);
			if(enableDebug)System.out.println("SCORE:"+score);
			uniScore = score;
			if(enableDebug)System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			Integer[] playerReturn = new Integer[1];
			playerReturn[0] = maxPlayer;
			nextState.add(playerReturn);
			nextState.add(originalBoard.get(0));
			nextState.add(originalBoard.get(1));
			Integer[] nextMancalaOne = new Integer[1];
			Integer[] nextMancalaTwo = new Integer[1];
			nextMancalaOne[0] = originalMancala.get(0);
			nextMancalaTwo[0] = originalMancala.get(1);
			nextState.add(nextMancalaOne);
			nextState.add(nextMancalaTwo);
		
			if(enableDebug)System.out.println(originalMancala.get(0)+" and "+originalMancala.get(1));
			printBoard(originalBoard,originalMancala,maxPlayer);
			
		}	
		
	return score;	
	
	}
	
	public static void pruning(ArrayList<String> inputList){
		print("Hello from alpha beta pruning");
		int player = Integer.parseInt(inputList.get(1));
		int maxDepth = Integer.parseInt(inputList.get(2));
		String[] playerTwoBoard = inputList.get(3).split("\\s+");			
		String[] playerOneBoard = inputList.get(4).split("\\s+");
		Integer[] boardOne = new Integer[playerOneBoard.length];
		Integer[] boardTwo = new Integer[playerTwoBoard.length];
		int boardSize = playerOneBoard.length;
		int mancalaTwo = Integer.parseInt(inputList.get(5));
		int mancalaOne = Integer.parseInt(inputList.get(6));
		ArrayList<Integer> originalMancala = new ArrayList<Integer>();
		originalMancala.add(mancalaOne);
		originalMancala.add(mancalaTwo);
		for(int i=0;i<playerOneBoard.length;i++){
			boardOne[i] = Integer.parseInt(playerOneBoard[i]);
			boardTwo[i] = Integer.parseInt(playerTwoBoard[i]);
		}
		ArrayList<Integer[]> originalBoard = new ArrayList<Integer[]>();
		originalBoard.add(boardOne);
		originalBoard.add(boardTwo);
		int currentDepth = 0;
		int maxPlayer = player;
		String pitName = "root";
		int alpha = (int)Double.NEGATIVE_INFINITY;
		int beta = (int)Double.POSITIVE_INFINITY;
		int bestVal = pruningDecision(player, maxPlayer,originalBoard, originalMancala,boardSize,maxDepth,currentDepth,pitName,false,alpha,beta);
		if(enableDebug)System.out.println("Best Val is:"+bestVal);
		//for(traverseLog t:tl){
		//	t.printLog();
		//}
	}
	
	public static ArrayList<traverseLog> tlg = new ArrayList<traverseLog>();
	public static ArrayList<Integer[]> nextStateAB = new ArrayList<Integer[]>();
	
	public static int uniScoreAB = (int)Double.NEGATIVE_INFINITY;
	public static int pruningDecision(int player, int maxPlayer,ArrayList<Integer[]> originalBoard, ArrayList<Integer> originalMancala, int boardSize, int maxDepth, int currentDepth, String pitName, boolean rec, int alpha, int beta){
		int score = 0;
		if(enableDebug)System.out.println("alpha:"+alpha+" and beta"+beta);
		if(gameOver(originalBoard)){
			print("Game Over!");
			//printBoard(originalBoard,originalMancala,player);
			score = calculateUtility(originalMancala,maxPlayer);
			if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+","+score+","+alpha+","+beta);
			traverseLog x= new traverseLog(pitName,currentDepth,score,alpha,beta);
			tlg.add(x);
		}
		else if(terminalTest(currentDepth,maxDepth) && (!rec)){
			print("Terminal Test and previous call not recursive from alpha beta!");
			score = calculateUtility(originalMancala,maxPlayer);
			if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+","+score+","+alpha+","+beta);
			traverseLog x= new traverseLog(pitName,currentDepth,score,alpha,beta);
			tlg.add(x);
		}
		else{
			int bestScore = 0;
			int v = 0;
			//ArrayList<Integer[]> bestState = new ArrayList<Integer[]>();
			int bestMancalaOne=0;
			int bestMancalaTwo=0;
			if(!rec){
				if(player == maxPlayer){
					bestScore = (int)Double.NEGATIVE_INFINITY;
					//Enter NodeName, depth, value to traverseLog
					//print("Eval value:"+bestScore);
					if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+",-Infinity,"+alpha+","+beta);
					traverseLog x= new traverseLog(pitName,currentDepth,bestScore,alpha,beta);
					tlg.add(x);
				}
				else{
					bestScore = (int)Double.POSITIVE_INFINITY;
					//Enter node name, depth, value to traverseLog
					//print("Eval value:"+bestScore);
					if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+",Infinity,"+alpha+","+beta);
					traverseLog x= new traverseLog(pitName,currentDepth,bestScore,alpha,beta);
					tlg.add(x);
				}
			}
			else{
				if(currentDepth==maxDepth){
					if(player == maxPlayer){
						bestScore = (int)Double.NEGATIVE_INFINITY;
						if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+",-Infinity,"+alpha+","+beta);
						traverseLog x= new traverseLog(pitName,currentDepth,bestScore,alpha,beta);
						tlg.add(x);
					}
					else{
						bestScore = (int)Double.POSITIVE_INFINITY;
						if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+",Infinity,"+alpha+","+beta);
						traverseLog x= new traverseLog(pitName,currentDepth,bestScore,alpha,beta);
						tlg.add(x);
					}
				}
				else{
					if(player == maxPlayer){
						bestScore = (int)Double.NEGATIVE_INFINITY;
						if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+",-Infinity,"+alpha+","+beta);
						traverseLog x= new traverseLog(pitName,currentDepth,bestScore,alpha,beta);
						tlg.add(x);
					}
					else{
						bestScore = (int)Double.POSITIVE_INFINITY;
						if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+",Infinity,"+alpha+","+beta);
						traverseLog x= new traverseLog(pitName,currentDepth,bestScore,alpha,beta);
						tlg.add(x);
					}
				}
				//Enter node name, depth, value to traverseLog
				print("Eval value:"+bestScore);
			}
			if(enableDebug)System.out.println("get traversal order for player:"+player);
			ArrayList<Integer> traversalOrder = new ArrayList<Integer>();
			
			if(player == 1){
					for(Integer t=0;t<boardSize;t++){
						traversalOrder.add(t);
					}
			}
			else{
				for(Integer t=boardSize-1;t>=0;t--){
					traversalOrder.add(t);
				}
			}
			if(enableDebug)System.out.println("got traversal order:");
			int recursiveScore = 0;
			int opponent= 0;
			boolean prune = false;
			for(int j:traversalOrder){
				
				int selectedPit = j;
				if(enableDebug)System.out.println("DEPTH"+currentDepth);
				//if(enableDebug)System.out.println("Original: ");
				//printBoard(originalBoard,originalMancala,player);
				Integer[] playerCurrent = new Integer[boardSize];
				Integer[] playerOpponent=new Integer[boardSize];
				String s = "";
				//int opponent=0;
				//this is done when recursion is not true
				if(pitName.compareTo("root")==0){
					if(player==1){
						int k = j + 2;
						s="B"+k;
						for(int i=0;i<boardSize;i++){
							playerCurrent[i] = originalBoard.get(0)[i];
							playerOpponent[i] = originalBoard.get(1)[i];
						}
						opponent = 2;
					}
					else{
						int k = boardSize - j -1+2;
						s = "A"+k;
						for(int i=0;i<boardSize;i++){
							playerCurrent[boardSize-i-1] = originalBoard.get(1)[i];
							playerOpponent[boardSize-i-1] = originalBoard.get(0)[i];
						}
						opponent = 1;
						
					}

				}
				else{
					if(player==1 && rec){
						int k = j + 2;
						s="B"+k;
						for(int i=0;i<boardSize;i++){
							playerCurrent[i] = originalBoard.get(0)[i];
							playerOpponent[i] = originalBoard.get(1)[i];
						}
						opponent = 2;
					}
					else if(player==1 && !rec){
						int k = j + 2;
						s="B"+k;
						for(int i=0;i<boardSize;i++){
							playerCurrent[boardSize-i-1] = originalBoard.get(0)[i];
							playerOpponent[boardSize-i-1] = originalBoard.get(1)[i];
						}
						opponent = 2;
						
					}



					else if(player==2 && rec){
						int k = boardSize - j -1+2;
						s = "A"+k;
						for(int i=0;i<boardSize;i++){
							playerCurrent[i] = originalBoard.get(1)[i];
							playerOpponent[i] = originalBoard.get(0)[i];
						}
						opponent = 1;
					}


					else if(player==2 && !rec){
						int k = boardSize - j -1+2;
						s = "A"+k;
						for(int i=0;i<boardSize;i++){
							playerCurrent[boardSize-i-1] = originalBoard.get(1)[i];
							playerOpponent[boardSize-i-1] = originalBoard.get(0)[i];
						}
						opponent = 1;
					}

				}
				print("Selected pit:"+s);
				boolean flag=true;
				int count = playerCurrent[selectedPit];
				while(flag){
					if(count==0){
						flag = false;
						print("Try another state");
					}
					else{
						if(count<(boardSize-selectedPit)){
							boolean doRecursion = false;
							int next = 1;
							print("From Case 1:");
							playerCurrent[selectedPit]=0;
							int numberOfStones=0;
							while(next<=count){
								//if the last stone is being placed on an empty pit then remove that stone and all the stones in the opposite pit and add to mancala
								if(next==count && playerCurrent[selectedPit+next]==0){
									numberOfStones = playerOpponent[selectedPit+next]+1;
									playerOpponent[selectedPit+next]=0;
									next=next+1;
								}
								else{
									playerCurrent[selectedPit+next] = playerCurrent[selectedPit+next] + 1;
									next=next+1;
								}
							}
							int opponentMancala = 0;
							if(isUnstableState(playerCurrent)){
								for(int z=0; z<boardSize;z++){
									opponentMancala = opponentMancala + playerOpponent[z];
									playerOpponent[z] = 0;
								}
							}
							int currentMancala = 0;
							if(isUnstableState(playerOpponent)){
								for(int z=0; z<boardSize;z++){
									currentMancala = currentMancala + playerCurrent[z];
									playerCurrent[z] = 0;
								}
							}
							ArrayList<Integer[]> newBoard = new ArrayList<Integer[]>();
							ArrayList<Integer> newMancala = new ArrayList<Integer>();
							if(player==1){
								newBoard.add(playerCurrent);
								newBoard.add(playerOpponent);	
								newMancala.add(originalMancala.get(player-1)+numberOfStones+currentMancala);
								newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
							}
							else{
								newBoard.add(playerOpponent);
								newBoard.add(playerCurrent);
								newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
								newMancala.add(originalMancala.get(player-1)+numberOfStones + currentMancala);			
							}
							printBoard(newBoard,newMancala,player);
							if(rec && !doRecursion){
								recursiveScore = pruningDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,doRecursion,alpha,beta);
							}
							else if(!rec && !doRecursion){
								recursiveScore = pruningDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion,alpha,beta);
							}
							else if(rec && doRecursion){
								recursiveScore = pruningDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,doRecursion,alpha,beta);
							}
							else if(!rec && doRecursion){
								recursiveScore = pruningDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion,alpha,beta);
							}
							//recursiveScore = minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion);
					
							if(player==maxPlayer){
								if(recursiveScore>bestScore){
									bestScore = recursiveScore;
								}
								if(bestScore>=beta){
									prune = true;
								}
								else{
									if(bestScore>alpha){
										alpha=bestScore;
									}
								}
							}
							else{
								if(recursiveScore<bestScore){
									bestScore = recursiveScore;
								}
								if(bestScore<=alpha){
									prune=true;
								}
								else{
									if(bestScore<beta){
										beta=bestScore;
									}
								}
							}					
							if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+","+bestScore+","+alpha+","+beta);
							traverseLog x= new traverseLog(pitName,currentDepth,bestScore,alpha,beta);
							tlg.add(x);
							flag=false;
							newBoard.clear();	
						}
						else if(count==(boardSize-selectedPit)){
							int nextRecursionScore = 0;
							print("From case 2:");
							ArrayList<Integer[]> temp = new ArrayList<Integer[]>();
							int next = 1;
							playerCurrent[selectedPit]=0;
							while(next<count){
								playerCurrent[selectedPit+next] = playerCurrent[selectedPit+next] + 1;
								next=next+1;
							}
							boolean doRecursion = true;
							int opponentMancala = 0;
							if(isUnstableState(playerCurrent)){
								doRecursion = false;
								for(int i=0;i<boardSize;i++){
									opponentMancala = opponentMancala + playerOpponent[i];
									playerOpponent[i] = 0;
								}
							}
							
							int currentMancala = 0;
							if(isUnstableState(playerOpponent)){
								doRecursion = false;
								for(int i=0;i<boardSize;i++){
									currentMancala = currentMancala + playerCurrent[i];
									playerOpponent[i] = 0;
								}
							}
							ArrayList<Integer[]> newBoard = new ArrayList<Integer[]>();
							ArrayList<Integer> newMancala = new ArrayList<Integer>();
							
							if(player==1){
								newBoard.add(playerCurrent);
								newBoard.add(playerOpponent);
								newMancala.add(originalMancala.get(player-1)+1+currentMancala);
								newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
							}
							else{
								newBoard.add(playerOpponent);
								newBoard.add(playerCurrent);
								newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
								newMancala.add(originalMancala.get(player-1)+1+currentMancala);
							}
							printBoard(newBoard,newMancala,player);
							if(enableDebug)System.out.println(" Value of doRecursion"+doRecursion);
							if(enableDebug)System.out.println(" Value of depth"+currentDepth);
							if(rec && !doRecursion){
								recursiveScore = pruningDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,doRecursion,alpha,beta);
							}
							else if(!rec && !doRecursion){
								recursiveScore = pruningDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion,alpha,beta);
							}
							else if(rec && doRecursion){
								recursiveScore = pruningDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,doRecursion,alpha,beta);
							}
							else if(!rec && doRecursion){
								recursiveScore = pruningDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion,alpha,beta);
							}
							/*if(doRecursion){
								recursiveScore = minimaxDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,doRecursion);
								//minimaxDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,doRecursion);
							}
							else{
								recursiveScore = minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion);
								//minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,doRecursion);
							}*/
							if(player==maxPlayer){
								if(recursiveScore>bestScore){
									bestScore = recursiveScore;
								}
								if(bestScore>=beta){
									prune = true;
								}
								else{
									if(bestScore>alpha){
										alpha=bestScore;
									}
								}
							}
							else{
								if(recursiveScore<bestScore){
									bestScore = recursiveScore;
								}
								if(bestScore<=alpha){
									prune=true;
								}
								else{
									if(bestScore<beta){
										beta=bestScore;
									}
								}
							}				
							if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+","+bestScore+","+alpha+","+beta);
							traverseLog x= new traverseLog(pitName,currentDepth,bestScore,alpha,beta);
							tlg.add(x);
							flag = false;		
						}
						else if(count>(boardSize - selectedPit)){
							if(enableDebug)System.out.println("From case 3");
							ArrayList<Integer[]> temp = new ArrayList<Integer[]>();
							int remaining = count;
							int next = 1;
							int currentStones = originalMancala.get(player-1);
							playerCurrent[selectedPit]=0;
							boolean curr= true;
							boolean opp = false;
							int prev = 0;
							int currentMancala = 0;
							int opponentMancala = 0;
							boolean expcase = false;
							boolean exceptionalCase = false;
							while(remaining>0){
								if(curr){
									if(selectedPit+next==boardSize){
										if(remaining-1==0){
											exceptionalCase = true;
											//if this is the7 last stone.. update the board and call recursive func
											int currentStonesR = currentStones + 1;
											if(isUnstableState(playerCurrent)){
												exceptionalCase = false;
												expcase = true;
												for(int i = 0;i<boardSize;i++){
													opponentMancala = opponentMancala + playerOpponent[i];
													playerOpponent[i] = 0;
												}
											}
											if(isUnstableState(playerOpponent)){
												exceptionalCase = false;
												expcase = true;
												for(int i =0 ;i<boardSize;i++){
													currentMancala = currentMancala + playerCurrent[i];
													playerCurrent[i]=0;
												}
												//currentMancala = currentMancala + 1 ;
											}
											//Check for unstable state.. if any state playerCurrent or playerOpponent is all zeros then return value and do not recurse
											if(exceptionalCase){
												ArrayList<Integer> newMancalaR = new ArrayList<Integer>();
												ArrayList<Integer[]> newBoardR = new ArrayList<Integer[]>();
												if(player==1){
													newBoardR.add(playerCurrent);
													newBoardR.add(playerOpponent);
													newMancalaR.add(currentStonesR);
													newMancalaR.add(originalMancala.get(opponent-1));
												}
												else{
													newBoardR.add(playerOpponent);
													newBoardR.add(playerCurrent);
													newMancalaR.add(originalMancala.get(opponent-1));
													newMancalaR.add(currentStonesR);
												}
												
												if(rec && !exceptionalCase){
													recursiveScore = pruningDecision(opponent,maxPlayer,newBoardR,newMancalaR,boardSize,maxDepth,currentDepth,s,exceptionalCase,alpha,beta);
												}
												else if(!rec && !exceptionalCase){
													recursiveScore = pruningDecision(opponent,maxPlayer,newBoardR,newMancalaR,boardSize,maxDepth,currentDepth+1,s,exceptionalCase,alpha,beta);
												}
												else if(rec && exceptionalCase){
													recursiveScore = pruningDecision(player,maxPlayer,newBoardR,newMancalaR,boardSize,maxDepth,currentDepth,s,exceptionalCase,alpha,beta);
												}
												else if(!rec && exceptionalCase){
													recursiveScore = pruningDecision(player,maxPlayer,newBoardR,newMancalaR,boardSize,maxDepth,currentDepth+1,s,exceptionalCase,alpha,beta);
												}
												//recursiveScore = minimaxDecision(player,maxPlayer,newBoardR,newMancalaR,boardSize,maxDepth,currentDepth,s,exceptionalCase);
												
											}
											//evalFunctionValue = newMancala.get(0) - newMancala.get(1);
											//if(enableDebug)System.out.println("Eval value is:"+evalFunctionValue);
											
										}
										currentStones = currentStones + 1 +currentMancala;
										remaining=remaining -1;
										if(remaining>0){
											opp = true;
											prev = boardSize -1;
										}
										curr = false;
									}
									else if(selectedPit+next<boardSize){
										if(remaining-1==0 && playerCurrent[selectedPit+next]==0){
											currentStones = currentStones + playerOpponent[selectedPit+next]+1;
											playerOpponent[selectedPit+next]=0;
											remaining = remaining - 1;
											next = next+1;
										}
										else{
											playerCurrent[selectedPit+next] = playerCurrent[selectedPit+next] + 1;
											remaining = remaining-1;
											next=next+1;
										}
									}
									
								}
								else if(opp){
									if(prev>=0){
										playerOpponent[prev] = playerOpponent[prev]+1;
										remaining = remaining-1;
										prev = prev - 1;
									}
									else{
										if(remaining>0){
											curr= true;
											next = 0;
											selectedPit = 0;
										}
									}
								}
							}
							if(!expcase){
								if(isUnstableState(playerCurrent)){
									for(int i = 0;i<boardSize;i++){
										opponentMancala = opponentMancala+playerOpponent[i];
										playerOpponent[i] = 0;
									}
								}
								if(isUnstableState(playerOpponent)){
									for(int i=0;i<boardSize;i++){
										currentMancala = currentMancala+playerCurrent[i];
										playerCurrent[i]=0;
									}
									currentStones = currentStones + currentMancala;
								}
							}
							ArrayList<Integer[]> newBoard = new ArrayList<Integer[]>();
							ArrayList<Integer> newMancala = new ArrayList<Integer>();
							if(player==1){
								newBoard.add(playerCurrent);
								newBoard.add(playerOpponent);
								newMancala.add(currentStones);
								newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
							}
							else{
								newBoard.add(playerOpponent);
								newBoard.add(playerCurrent);
								newMancala.add(originalMancala.get(opponent-1)+opponentMancala);
								newMancala.add(currentStones);
							}
							printBoard(newBoard,newMancala,player);
							if(!exceptionalCase){
									if(rec && !exceptionalCase){
										recursiveScore = pruningDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,exceptionalCase,alpha,beta);
									}
									else if(!rec && !exceptionalCase){
										recursiveScore = pruningDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,exceptionalCase,alpha,beta);
									}
									else if(rec && exceptionalCase){
										recursiveScore = pruningDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth,s,exceptionalCase,alpha,beta);
									}
									else if(!rec && exceptionalCase){
										recursiveScore = pruningDecision(player,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,exceptionalCase,alpha,beta);
									}
								//minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,exceptionalCase);
								//recursiveScore = minimaxDecision(opponent,maxPlayer,newBoard,newMancala,boardSize,maxDepth,currentDepth+1,s,exceptionalCase);
							}
							if(player==maxPlayer){
								if(recursiveScore>bestScore){
									bestScore = recursiveScore;
								}
								if(bestScore>=beta){
									prune = true;
								}
								else{
									if(bestScore>alpha){
										alpha=bestScore;
									}
								}
							}
							else{
								if(recursiveScore<bestScore){
									bestScore = recursiveScore;
								}
								if(bestScore<=alpha){
									prune=true;
								}
								else{
									if(bestScore<beta){
										beta=bestScore;
									}
								}
							}				
							if(enableDebug)System.out.println("TraverseLog:"+pitName+","+currentDepth+","+bestScore+","+alpha+","+beta);
							traverseLog x= new traverseLog(pitName,currentDepth,bestScore,alpha,beta);
							tlg.add(x);
							flag = false;
						}		
					}
				}	
				if(prune){
					break;
				}
			}
			score = bestScore;
			
		}
		
		if(currentDepth==1 && score>uniScoreAB){
			nextStateAB.clear();
			if(enableDebug)System.out.println("UNI:"+uniScoreAB);
			if(enableDebug)System.out.println("SCORE:"+score);
			uniScoreAB = score;
			if(enableDebug)System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			Integer[] playerReturn = new Integer[1];
			playerReturn[0] = maxPlayer;
			nextStateAB.add(playerReturn);
			nextStateAB.add(originalBoard.get(0));
			nextStateAB.add(originalBoard.get(1));
			Integer[] nextMancalaOne = new Integer[1];
			Integer[] nextMancalaTwo = new Integer[1];
			nextMancalaOne[0] = originalMancala.get(0);
			nextMancalaTwo[0] = originalMancala.get(1);
			nextStateAB.add(nextMancalaOne);
			nextStateAB.add(nextMancalaTwo);
		
			if(enableDebug)System.out.println(originalMancala.get(0)+" and "+originalMancala.get(1));
			printBoard(originalBoard,originalMancala,maxPlayer);
			
		}	
		
	return score;	
	
	}

	
	

	
	public static boolean terminalTest(int currentDepth, int maxDepth){
		boolean cutOff = false;
		if(currentDepth==maxDepth){
			cutOff=true;
		}
		else{
			cutOff = false;
		}
		return cutOff;
	}
	
	public static int calculateUtility(ArrayList<Integer> newMancala, int maxPlayer){
		int utility = 0;
		if(maxPlayer==1){
			utility = newMancala.get(0)-newMancala.get(1);
		}
		else{
			utility = newMancala.get(1)-newMancala.get(0);
		}
		return utility;
	}
	/*public static boolean gameOver(ArrayList<Integer[]> board){
		boolean val = true; 
		for(int i=0;i<board.get(0).length;i++){
			if(board.get(0)[i]==0 && board.get(1)[i]==0){
				val = true;
			}
			else{
				val = false;
			}
		}
		return val;
	}*/
	public static boolean gameOver(ArrayList<Integer[]> board){
		boolean val = true; 
		for(int i=0;i<board.get(0).length;i++){
			if(board.get(0)[i]==0 && board.get(1)[i]==0){
				val = true;
				continue;
			}
			else{
				val = false;
				break;
			}
		}
		return val;
	}
	
	
	
}

class traverseLog{
	String nodeState;
	Integer depth;
	int value;
	int alpha;
	int beta;
	
	public traverseLog(String state, Integer dep, int val){
		this.nodeState = state;
		this.depth = dep;
		this.value = val;
	}
	public traverseLog(String state, Integer dep, int val, int a,int b){
		this.nodeState = state;
		this.depth = dep;
		this.value = val;
		this.alpha = a;
		this.beta = b;
	}
	public void printLog(){
		boolean enableDebug = false;
		if(value==(int)Double.POSITIVE_INFINITY){
			if(enableDebug)System.out.println(nodeState+","+depth+",Infinity");
		}
		else if(value==(int)Double.NEGATIVE_INFINITY){
			if(enableDebug)System.out.println(nodeState+","+depth+",-Infinity");
		}
		else{
			if(enableDebug)System.out.println(nodeState+","+depth+","+value);
		}
	}
	public void printLogAB(){
		boolean enableDebug = false;
		if(value==(int)Double.POSITIVE_INFINITY){
			if(enableDebug)System.out.println(nodeState+","+depth+",Infinity,"+alpha+","+beta);
		}
		else if(value==(int)Double.NEGATIVE_INFINITY){
			if(enableDebug)System.out.println(nodeState+","+depth+",-Infinity,"+alpha+","+beta);
		}
		else{
			if(enableDebug)System.out.println(nodeState+","+depth+","+value+","+alpha+","+beta);
		}
	}
	public String outputLog(){
		String s ="";
		if(value==(int)Double.POSITIVE_INFINITY){
			s = nodeState+","+depth+",Infinity";
		}
		else if(value==(int)Double.NEGATIVE_INFINITY){
			s = nodeState+","+depth+",-Infinity";
		}
		else{
			s = nodeState+","+depth+","+value;
		}
		return s;
	}
	
	public String outputLogAB(){
		String s ="";
		String valueString ="";
		String alphaString ="";
		String betaString ="";
		int positiveInfinity = (int)Double.POSITIVE_INFINITY;
		int negativeInfinity = (int)Double.NEGATIVE_INFINITY;
		switch(value){
			case (int)Double.POSITIVE_INFINITY: valueString = "Infinity";
									break;
			case (int)Double.NEGATIVE_INFINITY: valueString = "-Infinity";
									break;
			default:valueString = String.valueOf(value);
									break;
			
		}
		switch(alpha){
			case (int)Double.POSITIVE_INFINITY: alphaString = "Infinity";
									break;
			case (int)Double.NEGATIVE_INFINITY: alphaString = "-Infinity";
									break;
			default:alphaString = String.valueOf(alpha);
									break;
		}
		switch(beta){
			case (int)Double.POSITIVE_INFINITY: betaString = "Infinity";
									break;
			case (int)Double.NEGATIVE_INFINITY: betaString  = "-Infinity";
									break;
			default: betaString  = String.valueOf(beta);
									break;
		}
		
		s = nodeState+","+depth+","+valueString+","+alphaString+","+betaString;
		
		return s;
	}
	
}


