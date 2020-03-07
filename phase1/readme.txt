
Boundary Cases & Invalid Input for Trip GUI:
1 - tap in and out at the same station: not counted stored as a trip/a dialogue pop up to show user has to choose different entrance and exit
2 - Button Tap In and Tap Out, JComboBox selection of entrance and exit, JTextfield of enter and exit time
    a. user can't only click tap in and then click back
    b. user can't only click tap out without clicking tap in first 
    c. user can't click tap in more than one time
    d. user can't click tap out more than one time
    e. user can't click tap in without inputing enter time
    f. user can't click tap out without inputing exit time
    g. user can't input enter time or exit time which violates the simple date format
    h. user can't input exit time which is early than the enter time 
    i. user can click back if neither tap in nor tap out button clicked or both of tap in and tap out button clicked with correct information input
    j. user will receive balance warning dialog if the using card's balance is less than zero after one taping in/taping out's charge 
    k. user cannot use a card to tap in of which the balance is negative until the balance is added
    l. user cannot use different card to tap in and tap out 



Test 1: Add New Card
Test 2: (Only Subway Trip Check) Subway Line 1 enter: 2018-07-01 11:31:50   exit: 2018-07-01 11:41:50
Test 3: (Only Bus Trip Check) Bus Line 1 enter: 2018-07-01 11:51:50   exit: 2018-07-01 11:55:50
Test 4: (Transfer Trip Check) Subway Line 1: enter: 2018-07-01 12:31:50   exit: 2018-07-01 12:33:50  Bus Line 1: enter: 2018-07-01 12:51:50   exit: 2018-07-01 12:55:50
Test 5: (Only Subway Trip Check fare up to the CAP of $6) Subway Line 1: enter: 2018-07-02 12:31:50   exit: 2018-07-02 14:45:50
Test 5: (Only Subway Trip Check fare up to the CAP of $6) Subway Line 1: enter: 2018-07-02 12:31:50   exit: 2018-07-02 14:45:50
Test 7: boundary case: b. user can't only click tap out without clicking tap in first 
Test 8: 