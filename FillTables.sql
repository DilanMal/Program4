/*
 * This file is a script that adds example tuples to the arcade DB.
 */

SET DEFINE OFF
SET LINESIZE 500
SET PAGESIZE 500
SET AUTOCOMMIT OFF

/* 
 * Erasing contents in object tables. 
 */
DELETE FROM dilanm.Membership;
DELETE FROM dilanm.Game;
DELETE FROM dilanm.Coupon;
DELETE FROM dilanm.Prize;

/* 
 * Erasing contents in transaction tables. 
 */
DELETE FROM dilanm.GameXact;
DELETE FROM dilanm.CouponXact;
DELETE FROM dilanm.PrizeXact;
DELETE FROM dilanm.TTXact;

/* 
 * Inserting games. 
 */
INSERT INTO dilanm.Game (GameID, GName, TokenCost)
       VALUES (11930, 'Pinball', 1);

INSERT INTO dilanm.Game (GameID, GName, TokenCost)
       VALUES (11908, 'Skeeball', 2);

INSERT INTO dilanm.Game (GameID, GName, TokenCost)
       VALUES (11948, 'Mini Basketball', 3);

INSERT INTO dilanm.Game (GameID, GName, TokenCost)
       VALUES (11980, 'Pacman', 4);

INSERT INTO dilanm.Game (GameID, GName, TokenCost)
       VALUES (11993, 'Escape From Jurassic Park', 3);

INSERT INTO dilanm.Game (GameID, GName, TokenCost)
       VALUES (11977, 'Star Wars: Trench Run', 4);

INSERT INTO dilanm.Game (GameID, GName, TokenCost)
       VALUES (11966, 'Star Trek: Starfleet Skirmish', 2);

/* 
 * Inserting prizes. 
 */
INSERT INTO dilanm.Prize (PrizeID, PName, TicketCost)
       VALUES (10101, 'Pacman Plushy', 1000);

INSERT INTO dilanm.Prize (PrizeID, PName, TicketCost)
       VALUES (20202, '1lb of Mixed Candy', 2000);

INSERT INTO dilanm.Prize (PrizeID, PName, TicketCost)
       VALUES (30303, 'Basketball', 3000);

INSERT INTO dilanm.Prize (PrizeID, PName, TicketCost)
       VALUES (40404, 'Apple Vision Pro', 4000);

INSERT INTO dilanm.Prize (PrizeID, PName, TicketCost)
       VALUES (50505, 'Samsung Galaxy S4', 5000);

INSERT INTO dilanm.Prize (PrizeID, PName, TicketCost)
       VALUES (60606, 'Headphones', 500);

INSERT INTO dilanm.Prize (PrizeID, PName, TicketCost)
       VALUES (70707, 'Coffee Mug', 500);

/* 
 * Inserting coupons. 
 */
INSERT INTO dilanm.Coupon (CouponID, FoodItem)
       VALUES (0, '1 Free Beverage');
       
INSERT INTO dilanm.Coupon (CouponID, FoodItem)
       VALUES (1, '1 Free Pizza Slice');
       
INSERT INTO dilanm.Coupon (CouponID, FoodItem)
       VALUES (2, '1 Free Garlic Bread');
       
INSERT INTO dilanm.Coupon (CouponID, FoodItem)
       VALUES (3, '1 Free Ice Cream');
       
INSERT INTO dilanm.Coupon (CouponID, FoodItem)
       VALUES (4, '1 Free Cookie');
       
INSERT INTO dilanm.Coupon (CouponID, FoodItem)
       VALUES (5, '1 Free Milk Shake');
       
INSERT INTO dilanm.Coupon (CouponID, FoodItem)
       VALUES (6, '1 Free Fried Cheese Stick');

/* 
 * Inserting memberships. 
 */
INSERT INTO dilanm.Membership (MemberID, Name, TeleNum, Addr, TotalSpend, LastDate, Tickets, Tokens)
       VALUES (10000, 'Priyansh', 1023456789, '1234 N St', 0, TO_DATE('12/31/2023', 'MM/DD/YYYY'), 0, 0);

INSERT INTO dilanm.Membership (MemberID, Name, TeleNum, Addr, TotalSpend, LastDate, Tickets, Tokens)
       VALUES (10001, 'Jake', 1234567890, '5678 S St', 0, TO_DATE('1/25/2024', 'MM/DD/YYYY'), 0, 0);

INSERT INTO dilanm.Membership (MemberID, Name, TeleNum, Addr, TotalSpend, LastDate, Tickets, Tokens)
       VALUES (10002, 'Ahmoud', 2345678901, '9012 E St', 0, TO_DATE('2/26/2024', 'MM/DD/YYYY'), 0, 0); 

INSERT INTO dilanm.Membership (MemberID, Name, TeleNum, Addr, TotalSpend, LastDate, Tickets, Tokens)
       VALUES (10003, 'Noah', 3456789012, '3456 W St', 0, TO_DATE('3/26/2024', 'MM/DD/YYYY'), 0, 0);

INSERT INTO dilanm.Membership (MemberID, Name, TeleNum, Addr, TotalSpend, LastDate, Tickets, Tokens)
       VALUES (10004, 'Aditya', 4567890123, '7890 N St', 0, TO_DATE('4/26/2024', 'MM/DD/YYYY'), 0, 0);

INSERT INTO dilanm.Membership (MemberID, Name, TeleNum, Addr, TotalSpend, LastDate, Tickets, Tokens)
       VALUES (10005, 'Dilan', 5678901234, '1234 S St', 0, TO_DATE('4/26/2024', 'MM/DD/YYYY'), 0, 0);

INSERT INTO dilanm.Membership (MemberID, Name, TeleNum, Addr, TotalSpend, LastDate, Tickets, Tokens)
       VALUES (10006, 'Lester', 6789012345, '5678 E St', 0, TO_DATE('4/26/2024', 'MM/DD/YYYY'), 0, 0);

/* $10 = 1 Token */

/* 
 * Inserting transactions for Member 10000. 
 */
INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       VALUES (00001, 10000, 'tokens', 250, TO_DATE('12/31/2023', 'MM/DD/YYYY'));

       UPDATE dilanm.Membership
       SET TotalSpend = (SELECT TotalSpend
	    	         FROM dilanm.Membership
	    		 WHERE MemberID = 10000) + 250,
	   Tokens = (SELECT Tokens
                     FROM dilanm.Membership
                     WHERE MemberID = 1000) + (250 / 10)
       WHERE MemberID = 10000;

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00002, 10000, 'tickets', 5000, TO_DATE('12/31/2023', 'MM/DD/YYYY'));

	      UPDATE dilanm.Membership
	      SET Tickets = (SELECT Tickets
	      	  	     FROM dilanm.Membership
			     WHERE MemberID = 10000) + 5000
              WHERE MemberID = 10000;
	      
INSERT INTO dilanm.GameXact (GXactID, MemberID, GameID, Score)
       VALUES (00001, 10000, 11930, 5200);

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00003, 10000, 'tickets', 5200, TO_DATE('12/31/2023', 'MM/DD/YYYY'));

	      UPDATE dilanm.Membership
	      SET Tickets = (SELECT Tickets
		   	     FROM dilanm.Membership
		   	     WHERE MemberID = 10000) + 5200
	      WHERE MemberID = 10000;

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00004, 10000, 'tokens', (0 - (SELECT TokenCost
	       	      	      	     	            FROM dilanm.Game
						    WHERE GameID = 11930)), TO_DATE('12/31/2023', 'MM/DD/YYYY'));

	      UPDATE dilanm.Membership
	      SET Tokens = (SELECT Tokens
	       	   	    FROM dilanm.Membership
			    WHERE MemberID = 10000) - (SELECT TokenCost
			     	   	      	       FROM dilanm.Game
						       WHERE GameID = 11930)
	      WHERE MemberID = 10000;

INSERT INTO dilanm.GameXact (GXactID, MemberID, GameID, Score)
       VALUES (00002, 10000, 11908, 4300);

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00005, 10000, 'tickets', 4300, TO_DATE('12/31/2023', 'MM/DD/YYYY'));

              UPDATE dilanm.Membership
              SET Tickets = (SELECT Tickets
	       	   	     FROM dilanm.Membership
			     WHERE MemberID = 1000) + 4300
	      WHERE MemberID = 10000;

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
	      VALUES (00006, 10000, 'tokens', (0 - (SELECT TokenCost
	       	      	      	     	       	    FROM dilanm.Game
						    WHERE GameID = 11908)), TO_DATE('12/31/2023', 'MM/DD/YYYY'));

	      UPDATE dilanm.Membership
	      SET Tokens = (SELECT Tokens
                            FROM dilanm.Membership
                            WHERE MemberID = 10000) - (SELECT TokenCost
                                                       FROM dilanm.Game
                                                       WHERE GameID = 11908)
	      WHERE MemberID = 10000;

INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       VALUES (00007, 10000, 'tokens', 100, TO_DATE('12/31/2024', 'MM/DD/YYYY'));

       UPDATE dilanm.Membership
       SET TotalSpend = (SELECT TotalSpend
       	   	      	 FROM dilanm.Membership
			 WHERE MemberID = 10000) + (100 - (100 * 0.10)),
	   Tokens = (SELECT Tokens
	   	     FROM dilanm.Membership
		     WHERE MemberID = 10000) + (100 / 10)
       WHERE MemberID = 10000;

/*
 * Inserting transactions for Member 10002.
 */
INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       VALUES (00008, 10002, 'tokens', 500, TO_DATE('1/25/2024', 'MM/DD/YYYY'));

       UPDATE dilanm.Membership
       SET TotalSpend = (SELECT TotalSpend
	    	       	 FROM dilanm.Membership
			 WHERE MemberID = 10002) + 500,
	   Tokens = (SELECT Tokens
	    	     FROM dilanm.Membership
		     WHERE MemberID = 10002) + (500 / 10)
       WHERE MemberID = 10002;

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00009, 10002, 'tickets', 10000, TO_DATE('1/25/2024', 'MM/DD/YYYY'));

	      UPDATE dilanm.Membership
	      SET Tickets = (SELECT Tickets
	      	  	     FROM dilanm.Membership
			     WHERE MemberID = 10002) + 10000
	      WHERE MemberID = 10002;

INSERT INTO dilanm.GameXact (GXactID, MemberID, GameID, Score)
       VALUES (00003, 10002, 11948, 1234);

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00010, 10002, 'tickets', 1234, TO_DATE('1/27/2024', 'MM/DD/YYYY'));

	      UPDATE dilanm.Membership
	      SET Tickets = (SELECT Tickets
	       	   	     FROM dilanm.Membership
			     WHERE MemberID = 10002) + 1234
	      WHERE MemberID = 10002;

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00011, 10002, 'tokens', (0 - (SELECT TokenCost
	       	      	      	     	       	    FROM dilanm.Game
						    WHERE GameID = 11948)), TO_DATE('1/27/2024', 'MM/DD/YYYY'));

              UPDATE dilanm.Membership
	      SET Tokens = (SELECT Tokens
                            FROM dilanm.Membership
                            WHERE MemberID = 10002) - (SELECT TokenCost
                                                       FROM dilanm.Game
                                                       WHERE GameID = 11948)
	      WHERE MemberID = 10002;
	       
INSERT INTO dilanm.GameXact (GXactID, MemberID, GameID, Score)
       VALUES (00004, 10002, 11980, 5678);

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00012, 10002, 'tickets', 5678, TO_DATE('1/27/2024', 'MM/DD/YYYY'));

	      UPDATE dilanm.Membership
	      SET Tickets = (SELECT Tickets
	       	   	     FROM dilanm.Membership
			     WHERE MemberID = 10002) + 5678
	      WHERE MemberID = 10002;

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00013, 10002, 'tokens', (0 - (SELECT TokenCost
	       	      	      	     	       	    FROM dilanm.Game
						    WHERE GameID = 11980)), TO_DATE('1/27/2024', 'MM/DD/YYYY'));

              UPDATE dilanm.Membership
	      SET Tokens = (SELECT Tokens
                            FROM dilanm.Membership
                            WHERE MemberID = 10002) - (SELECT TokenCost
                                                       FROM dilanm.Game
                                                       WHERE GameID = 11980)
	      WHERE MemberID = 10002;

INSERT INTO dilanm.GameXact (GXactID, MemberID, GameID, Score)
       VALUES (00005, 10002, 11980, 100);

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
              VALUES (00014, 10002, 'tickets', 100, TO_DATE('1/27/2024', 'MM/DD/YYYY'));

              UPDATE dilanm.Membership
              SET Tickets = (SELECT Tickets
                             FROM dilanm.Membership
                             WHERE MemberID = 10002) + 100
              WHERE MemberID = 10002;

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
              VALUES (00015, 10002, 'tokens', (0 - (SELECT TokenCost
                                                    FROM dilanm.Game
                                                    WHERE GameID = 11980)), TO_DATE('1/27/2024', 'MM/DD/YYYY'));

              UPDATE dilanm.Membership
              SET Tokens = (SELECT Tokens
                            FROM dilanm.Membership
                            WHERE MemberID = 10002) - (SELECT TokenCost
                                                       FROM dilanm.Game
                                                       WHERE GameID = 11980)
              WHERE MemberID = 10002;

INSERT INTO dilanm.GameXact (GXactID, MemberID, GameID, Score)
       VALUES (00006, 10002, 11980, 50);

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
              VALUES (00016, 10002, 'tickets', 5678, TO_DATE('1/27/2024', 'MM/DD/YYYY'));

              UPDATE dilanm.Membership
              SET Tickets = (SELECT Tickets
                             FROM dilanm.Membership
                             WHERE MemberID = 10002) + 50
              WHERE MemberID = 10002;

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
              VALUES (00017, 10002, 'tokens', (0 - (SELECT TokenCost
                                                    FROM dilanm.Game
                                                    WHERE GameID = 11980)), TO_DATE('1/27/2024', 'MM/DD/YYYY'));

              UPDATE dilanm.Membership
              SET Tokens = (SELECT Tokens
                            FROM dilanm.Membership
                            WHERE MemberID = 10002) - (SELECT TokenCost
                                                       FROM dilanm.Game
                                                       WHERE GameID = 11980)
              WHERE MemberID = 10002;

INSERT INTO dilanm.PrizeXact (PXactID, MemberID, PrizeID)
       VALUES (00001, 10002, 70707);

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00018, 10002, 'tickets', (0 - (SELECT TicketCost
	       	      	      	     		     FROM dilanm.Prize
						     WHERE PrizeID = 70707)), TO_DATE('1/27/2024', 'MM/DD/YYYY'));
	      UPDATE dilanm.Membership
	      SET Tickets = (SELECT Tickets
	    	       	     FROM dilanm.Membership
		       	     WHERE MemberID = 10002) - (SELECT TicketCost
		       	     	      	       	      	FROM dilanm.Prize
						        WHERE PrizeID = 70707)
	      WHERE MemberID = 10002;

	DELETE FROM dilanm.Prize
	WHERE PrizeID = 70707;

INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       VALUES (00019, 10002, 'tokens', 100, TO_DATE('1/28/2024', 'MM/DD/YYYY'));

       UPDATE dilanm.Membership
       SET TotalSpend = (SELECT TotalSpend
       	   	      	 FROM dilanm.Membership
			 WHERE MemberID = 10002) + (100 - (100 * 0.20)),
           Tickets = (SELECT Tickets
	   	      FROM dilanm.Membership
		      WHERE MemberID = 10002) + (100 / 10)
       WHERE MemberID = 10002;
       
UPDATE dilanm.Membership
SET LastDate = TO_DATE('1/28/2024', 'MM/DD/YYYY')
WHERE MemberID = 10002;
 
/*
 * Inserting transactions for Member 10004.
 */
INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       VALUES (00020, 10004, 'tokens', 1000, TO_DATE('4/26/2024', 'MM/DD/YYYY'));

       UPDATE dilanm.Membership
       SET TotalSpend = (SELECT TotalSpend
	    	       	 FROM dilanm.Membership
			 WHERE MemberID = 10004) + 1000,
	   Tokens = (SELECT Tokens
	    	     FROM dilanm.Membership
		     WHERE MemberID = 10004) + (1000 / 10)
       WHERE MemberID = 10004;

INSERT INTO dilanm.GameXact (GXactID, MemberID, GameID, score)
       VALUES (00007, 10004, 11993, 9876);

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00021, 10004, 'tickets', 9876, TO_DATE('4/26/2024', 'MM/DD/YYYY'));

	      UPDATE dilanm.Membership
	      SET Tickets = (SELECT Tickets
	       	   	     FROM dilanm.Membership
			     WHERE MemberID = 10004) + 9876
	      WHERE MemberID = 10004;

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
	      VALUES (00022, 10004, 'tokens', (0 - (SELECT TokenCost
	       	      	      	     	       	    FROM dilanm.Game
						    WHERE GameID = 11993)), TO_DATE('4/27/2024','MM/DD/YYYY'));

              UPDATE dilanm.Membership
	      SET Tokens = (SELECT Tokens
	       	   	    FROM dilanm.Membership
			    WHERE MemberID = 10004) - (SELECT TokenCost
			     	   	      	       FROM dilanm.Game
						       WHERE GameID = 11993)
	      WHERE MemberID = 10004;

UPDATE dilanm.Membership
SET LastDate = TO_DATE('4/27/2024', 'MM/DD/YYYY')
WHERE MemberID = 10004;

/*
 * Inserting transactions for Member 10006.
 */
INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       VALUES (00023, 10006, 'tokens', 100, TO_DATE('4/27/2024', 'MM/DD/YYYY'));

       UPDATE dilanm.Membership
       SET TotalSpend = (SELECT TotalSpend
       	   	      	 FROM dilanm.Membership
			 WHERE MemberID = 10006) + 100,
           Tokens = (SELECT Tokens
       	   	     FROM dilanm.Membership
		     WHERE MemberID = 10006) + (100 / 10)
       WHERE MemberID = 10006;

INSERT INTO dilanm.GameXact (GXactID, MemberID, GameID, score)
       VALUES (00008, 10006, 11966, 10000);

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00024, 10006, 'tickets', 10000, TO_DATE('4/27/2024', 'MM/DD/YYYY'));

	      UPDATE dilanm.Membership
	      SET Tickets = (SELECT Tickets
	      	  	     FROM dilanm.Membership
			     WHERE MemberID = 10006) + 10000
	      WHERE MemberID = 10006;

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00025, 10006, 'tokens', (0 - (SELECT TokenCost
	      	     	     	    	      	    FROM dilanm.Game
						    WHERE GameID = 11966)), TO_DATE('4/27/2024', 'MM/DD/YYYY'));

              UPDATE dilanm.Membership
	      SET Tokens = (SELECT Tokens
	      	  	    FROM dilanm.Membership
			    WHERE MemberID = 10006) - (SELECT TokenCost
			    	  	     	       FROM dilanm.Game
						       WHERE GameID = 11966)
	      WHERE MemberID = 10006;

INSERT INTO dilanm.PrizeXact (PXactID, MemberID, PrizeID)
       VALUES (00002, 10006, 50505);

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00026, 10006, 'tickets', (0 - (SELECT TicketCost
	      	     	     	    	       	     FROM dilanm.Prize
						     WHERE PrizeID = 50505)), TO_DATE('4/27/2024', 'MM/DD/YYYY'));

	      UPDATE dilanm.Membership
	      SET Tickets = (SELECT Tickets
	      	  	     FROM dilanm.Membership
			     WHERE MemberID = 10006) - (SELECT TicketCost
			     	   	      	        FROM dilanm.Prize
							WHERE PrizeID = 50505)
	      WHERE MemberID = 10006;

       DELETE FROM dilanm.Prize
       WHERE PrizeID = 50505;

UPDATE dilanm.Membership
SET LastDate = TO_DATE('4/27/2024', 'MM/DD/YYYY')
WHERE MemberID = 10006;

/*
 * Inserting transactions for Member 10003.
 */
INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       VALUES (00027, 10003, 'tokens', 200, TO_DATE('4/27/2024', 'MM/DD/YYYY'));

       UPDATE dilanm.Membership
       SET TotalSpend = (SELECT TotalSpend
       	   	      	 FROM dilanm.Membership
			 WHERE MemberID = 10003) + 200,
	   Tokens = (SELECT Tokens
	   	     FROM dilanm.Membership
		     WHERE MemberID = 10003) + (200 / 10)
       WHERE MemberID = 10003;

INSERT INTO dilanm.GameXact (GXactID, MemberID, GameID, score)
       VALUES (00009, 10003, 11977, 4269);

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00028, 10003, 'tickets', 4269, TO_DATE('4/27/2024', 'MM/DD/YYYY'));

	      UPDATE dilanm.Membership
	      SET Tickets = (SELECT Tickets
	      	  	     FROM dilanm.Membership
			     WHERE MemberID = 10003) + 4269
	      WHERE MemberID = 10003;

       INSERT INTO dilanm.TTXact (TTXactID, MemberID, Category, Amount, TDate)
       	      VALUES (00029, 10003, 'tokens', (0 - (SELECT TokenCost
	      	     	     	    	      	    FROM dilanm.Game
						    WHERE GameID = 11977)), TO_DATE('4/27/2024', 'MM/DD/YYYY'));

	      UPDATE dilanm.Membership
	      SET Tokens = (SELECT Tokens
	      	  	    FROM dilanm.Membership
			    WHERE MemberID = 10003) - (SELECT TokenCost
			    	  	     	       FROM dilanm.Game
						       WHERE GameID = 11977)
	      WHERE MemberID = 10003;

UPDATE dilanm.Membership
SET LastDate = TO_DATE('4/27/2024', 'MM/DD/YYYY')
WHERE MemberID = 10003;

/*
 * Inserting Coupon transations.
 */
INSERT INTO dilanm.CouponXact (CXactID, MemberID, CouponID)
       VALUES (00001, 10000, 1);

INSERT INTO dilanm.CouponXact (CXactID, MemberID, CouponID)
       VALUES (00002, 10001, 5);
