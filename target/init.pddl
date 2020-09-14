(define (problem task)
(:domain rcll)
(:objects
    starts - starting
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
   (at r1 starts)
   (at r2 starts)
   (at r3 starts)
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

   (= (distance starts bs) 5)
   (= (distance starts ds) 5)
   (= (distance starts cs1_input) 5)
   (= (distance starts cs1_output) 5)
   (= (distance starts cs1_shelf) 5)

   (= (distance bs starts) 5)
   (= (distance bs ds) 5)
   (= (distance bs cs1_input) 5)
   (= (distance bs cs1_output) 5)
   (= (distance bs cs1_shelf) 5)

   (= (distance ds starts) 5)
   (= (distance ds bs) 5)
   (= (distance ds cs1_input) 5)
   (= (distance ds cs1_output) 5)
   (= (distance ds cs1_shelf) 5)

   (= (distance cs1_input starts) 5)
   (= (distance cs1_input ds) 5)
   (= (distance cs1_input bs) 5)
   (= (distance cs1_input cs1_output) 1)
   (= (distance cs1_input cs1_shelf) 1)

   (= (distance cs1_output starts) 5)
   (= (distance cs1_output ds) 5)
   (= (distance cs1_output bs) 5)
   (= (distance cs1_output cs1_input) 1)
   (= (distance cs1_output cs1_shelf) 1)

   (= (distance cs1_shelf starts) 5)
   (= (distance cs1_shelf ds) 5)
   (= (distance cs1_shelf bs) 5)
   (= (distance cs1_shelf cs1_input) 1)
   (= (distance cs1_shelf cs1_output) 1)

   

)


 
 

(:goal 
   (stage_c0_3 p1)
)

(:constraints 
	(preference p1 (sometime-after (locked cs1) (n_locked cs1)))
) 


(:metric minimize  (total-time) )

)
