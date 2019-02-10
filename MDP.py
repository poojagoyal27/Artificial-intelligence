import numpy as np
U=[-0.1,-0.1,1,1,-0.1,2,2,-0.1,-10,-10,10]
Reward=[-0.1,-0.1,1,1,-0.1,2,2,-0.1,-10,-10,10]
States=[1,2,2,2,2,5,5,5,5,8,8,8,8]
U_Modified=[]
##########
states = [1,2,3,4,5,6,7,8,9,10,11,12]
ActionsList = [1,2,3,4,5,6,7,8,9,10,11,12]
NO_STATES = len(states)
NO_ActionsList = len(ActionsList)
P = np.zeros((NO_STATES, NO_ActionsList, NO_STATES))  # transition probability
R = np.zeros(NO_STATES)  # rewards
s=[]


P[1,2,1] = 0.1
P[1,2,2] = 0.9

P[2,1,1] = 0.9
P[2,1,2] = 0.1
P[2,1,3] = 0.1
P[2,1,4] = 0.1

P[2,3,1] = 0.1
P[2,3,2] = 0.1
P[2,3,3] = 0.6
P[2,3,4] = 0.1
P[2,3,5] = 0.1

P[2,4,1] = 0.1
P[2,4,2] = 0.1
P[2,4,3] = 0.1
P[2,4,4] = 0.6
P[2,4,5] = 0.1

P[2,5,1] = 0.1
P[2,5,2] = 0.1
P[2,5,3] = 0.1
P[2,5,4] = 0.1
P[2,5,5] = 0.6

P[5,2,2] = 0.6
P[5,2,5] = 0.1
P[5,2,6] = 0.1
P[5,2,7] = 0.1
P[5,2,8] = 0.1

P[5,6,2] = 0.1
P[5,6,5] = 0.1
P[5,6,6] = 0.6
P[5,6,7] = 0.1
P[5,6,8] = 0.1

P[5,7,2] = 0.1
P[5,7,5] = 0.1
P[5,7,6] = 0.1
P[5,7,7] = 0.6
P[5,7,8] = 0.1

P[5,8,2] = 0.1
P[5,8,5] = 0.1
P[5,8,6] = 0.1
P[5,8,7] = 0.1
P[5,8,8] = 0.6

P[8,5,5] = 0.6
P[8,5,8] = 0.1
P[8,5,9] = 0.1
P[8,5,10] = 0.1
P[8,5,11] = 0.1

P[8,9,5] = 0.1
P[8,9,8] = 0.1
P[8,9,9] = 0.6
P[8,9,10] = 0.1
P[8,9,11] = 0.1

P[8,10,5] = 0.1
P[8,10,8] = 0.1
P[8,10,9] = 0.1
P[8,10,10] = 0.6
P[8,10,11] = 0.1

P[8,11,5] = 0.1
P[8,11,8] = 0.1
P[8,11,9] = 0.1
P[8,11,10] = 0.1
P[8,11,11] = 0.6


#for R in states
R[3] = 1
R[4] = 1
R[6] = 2
R[7] = 2
R[9] = -10
R[10] = -10
R[11] = 10
R[1] = -0.1
R[2] = -0.1
R[5] = -0.1
R[8] = -0.1


 
gamma = 1.0
#########

ActionsList=[[1, 2 ,[[1, 0.1], [2, 0.9]]],
[2 ,1 ,[[1, 0.7], [2, 0.1], [3, 0.1], [4, 0.1]]],
[2 ,3 ,[[1, 0.1], [2, 0.1], [3, 0.6], [4, 0.1], [5, 0.1]]],
[2 ,4 ,[[1, 0.1], [2, 0.1], [3, 0.1], [4, 0.6], [5, 0.1]]],
[2 ,5 ,[[1, 0.1], [2, 0.1], [3, 0.1], [4, 0.1], [5, 0.6]]],
[5 ,2 ,[[2, 0.6], [5, 0.1], [6, 0.1], [7, 0.1], [8, 0.1]]],
[5 ,6 ,[[2, 0.1], [5, 0.1], [6, 0.6], [7, 0.1], [8, 0.1]]],
[5 ,7 ,[[2, 0.1], [5, 0.1], [6, 0.1], [7, 0.6], [8, 0.1]]],
[5 ,8 ,[[2, 0.1], [5, 0.1], [6, 0.1], [7, 0.1], [8, 0.6]]],
[8 ,5 ,[[5, 0.6], [8, 0.1], [9, 0.1], [10, 0.1], [11, 0.1]]],
[8 ,9 ,[[5, 0.1], [8, 0.1], [9, 0.6], [10, 0.1], [11, 0.1]]],
[8 ,10 ,[[5, 0.1], [8, 0.1], [9, 0.1], [10, 0.6], [11, 0.1]]],
[8 ,11, [[5, 0.1], [8, 0.1], [9, 0.1], [10, 0.1], [11, 0.6]]]]
      
def ValueIter():
        print("Value Iteration Started!!!!!!!")
        for eachAction in ActionsList:
            temp=[]
            for eachPossibleAction in eachAction[2]:
                temp.append(eachPossibleAction[1]*U[eachPossibleAction[0]-1])
            UTemp=Reward[eachAction[0]]+(max(temp)*0.8)
            U_Modified.append([eachAction[0],UTemp])
        print("Value Iteration Final Output")
        print(U_Modified)
####################
def PolicyIter ():
    policy = [0 for s in range(NO_STATES)]
    V = np.zeros(NO_STATES)
    print("Policy Iteration Started!!")
    print ("Initial policy", policy)
    is_value_changed = True
    iterations = 0
    while is_value_changed:
        is_value_changed = False
        iterations += 1
        # run value iteration for each state
        for s in range(NO_STATES):
            V[s] = sum([P[s,policy[s],s1] * (R[s1] + gamma*V[s1]) for s1 in range(NO_STATES)])
            # print "Run for state", s
    
        for s in range(NO_STATES):
            q_best = V[s]
            # print "State", s, "q_best", q_best
            for a in range(NO_ActionsList):
                q_sa = sum([P[s, a, s1] * (R[s1] + gamma * V[s1]) for s1 in range(NO_STATES)])
                if q_sa > q_best:
                    print ("State", s, ": q_sa", q_sa, "q_best", q_best)
                    policy[s] = a
                    q_best = q_sa
                    is_value_changed = True
    
        print ("Iterations:", iterations)
        print ("Policy now", policy)
    
    print ("Final policy")
    print (policy)
    print (V)
    
    
ValueIter()
PolicyIter()



