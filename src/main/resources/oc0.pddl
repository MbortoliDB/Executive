(define (problem task)
(:domain rcll)
(:objects
    start - starting
    bs - base_station
    ds - delivery_station
    cs1_input - cap_station_input
    cs1_output - cap_station_output
    cs1_shelf - cap_station_shelf
    cs1 - cap_station
    r1 r2 r3 - robot 
    ;p1 p2 p3 p4 - product
    p1 - product
)
(:init
   (at r1 start)
   (at r2 start)
   (at r3 start)
   (free r1)
   (free r2)
   (free r3)
   (empty bs)
   (empty cs1_input)
   (empty cs1_output)
   (empty cs1_shelf)
   (empty ds)
   (stage_c0_0 p1)
   ;(stage_c0_0 p2)
   ;(stage_c0_0 p3)
   ;(stage_c0_0 p4)
   (n_holding r1)
   (n_holding r2)
   (n_holding r3)
   (parent_cs cs1_input cs1)
   (parent_cs cs1_output cs1)
   (parent_cs cs1_shelf cs1)
   (n_station_holding cs1)
   (n_cs_station_has_capbase cs1)


   
   (n_locked bs)
   (n_locked cs1)
   (n_locked ds)

   (completed cs1)

   (= (distance start bs) 5)
   (= (distance start ds) 5)
   (= (distance start cs1_input) 5)
   (= (distance start cs1_output) 5)
   (= (distance start cs1_shelf) 5)

   (= (distance bs start) 5)
   (= (distance bs ds) 5)
   (= (distance bs cs1_input) 5)
   (= (distance bs cs1_output) 5)
   (= (distance bs cs1_shelf) 5)

   (= (distance ds start) 5)
   (= (distance ds bs) 5)
   (= (distance ds cs1_input) 5)
   (= (distance ds cs1_output) 5)
   (= (distance ds cs1_shelf) 5)

   (= (distance cs1_input start) 5)
   (= (distance cs1_input ds) 5)
   (= (distance cs1_input bs) 5)
   (= (distance cs1_input cs1_output) 1)
   (= (distance cs1_input cs1_shelf) 1)

   (= (distance cs1_output start) 5)
   (= (distance cs1_output ds) 5)
   (= (distance cs1_output bs) 5)
   (= (distance cs1_output cs1_input) 1)
   (= (distance cs1_output cs1_shelf) 1)

   (= (distance cs1_shelf start) 5)
   (= (distance cs1_shelf ds) 5)
   (= (distance cs1_shelf bs) 5)
   (= (distance cs1_shelf cs1_input) 1)
   (= (distance cs1_shelf cs1_output) 1)

   

)



 

(:goal (and
   ;(stage_c0_1 p1)
   (stage_c0_3 p1)
   ;(finish cs1)
   ;(ff cs1)
   ;(stage_c0_3 p2)  
   ;(stage_c0_3 p3)
   ;(stage_c0_3 p4)
)
)

(:constraints 
	(always-within 45 (locked cs1) (n_locked cs1))

)  

(:metric minimize (total-time) )

)
