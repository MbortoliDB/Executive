(define (domain rcll)
 (:requirements :strips :typing :durative-actions :preferences :constraints :fluents :adl :numeric-fluents)
 (:types base_station cap_station_input cap_station_output cap_station_shelf delivery_station starting - location
   location cap_station - station
   robot product)

  (:predicates 
     (at ?r - robot ?l - location)
     (free ?r - robot)
     (empty ?l - location)
     (holding ?r - robot ?p - product)
     (n_holding ?r - robot)
     (holding_base ?r - robot)
     (holding_capbase ?r - robot)
     (station_holding ?cs - cap_station ?p - product)
     (n_station_holding ?cs - cap_station)
     (cs_station_has_capbase ?cs - cap_station)
     (n_cs_station_has_capbase ?cs - cap_station)
     (parent_cs ?l - location ?cs - cap_station)
     (stage_c0_0 ?p - product)
     (stage_c0_1 ?p - product)
     (stage_c0_2 ?p - product)
     (stage_c0_3 ?p - product)
     (locked ?s - station)
     (n_locked ?s - station)
     (finish ?s - station)
     (lll ?s - station)
     (completed ?s - station)
     (ff ?s - station)

 )

 (:functions
    (distance ?l1 ?l2 - location)
 )



(:durative-action move 
	:parameters (?r - robot ?from ?to - location )
	:duration (= ?duration (distance ?from ?to)) 
	:condition (and (at start (at ?r ?from)) (at start (free ?r)) (at end (empty ?to) )  )
	:effect (and
             (at start (not (at ?r ?from)))
             (at end (at ?r ?to))
	     (at start (not (free ?r)))
	     (at end (free ?r))
	     (at end (not (empty ?to)))
	     (at start (empty ?from))
 	)
)

(:durative-action lockStation
	:parameters (?s - station)
	:duration (= ?duration 0) 
	:condition (and 
		(at start (n_locked ?s))
	)
	:effect (and
		(at end (not (n_locked ?s)))
		(at end (locked ?s))
 	)
)




(:durative-action getBaseFromBScriticalTask 
	:parameters (?r - robot ?bs - base_station ?p - product)
	:duration (= ?duration 10) 
	:condition (and 
		(over all (locked ?bs))
		(at start (free ?r))
		(at start (stage_c0_0 ?p))
		(at start (at ?r ?bs))
		(at start (n_holding ?r))
	)
	:effect (and
		(at start (not (free ?r)))
		(at end (free ?r))
		(at end (stage_c0_1 ?p ))
		(at end (not (stage_c0_0 ?p)))
		(at end (holding ?r ?p))
		(at start (not (n_holding ?r)))
		(at end (not (locked ?bs)))
		(at end (n_locked ?bs))
 	)
)

(:durative-action toy1
	:parameters (?s - station)
	:duration (= ?duration 1) 
	:condition (and 
		(at start (completed ?s))
	)
	:effect (and
		(at start (not (completed ?s)))
		(at end (lll ?s))
 	)
)


(:durative-action toy2
	:parameters (?s - station)
	:duration (= ?duration 10) 
	:condition (and 
		(at start (lll ?s))
	)
	:effect (and
		(at end (completed ?s))
		;(at end (not (lll ?s)))
		(at end (ff ?s)) 	
	)
)




(:durative-action deliverProductToCScriticalTask 
	:parameters (?r - robot ?csi - cap_station_input ?cs - cap_station ?p - product)
	:duration (= ?duration 10) 
	:condition (and 
		(over all (locked ?cs))
		(at start (free ?r))
		(at start (parent_cs ?csi ?cs))
		(at start (holding ?r ?p))
		(at start (stage_c0_1 ?p))
		(at start (at ?r ?csi))
		(at start (n_station_holding ?cs))
		(at end (cs_station_has_capbase ?cs))		;maybe at start, check rules
	)
	:effect (and
		(at start (not (free ?r)))
		(at end (free ?r))
		(at start (not (holding ?r ?p)))
		(at end (n_holding ?r))
		(at end (station_holding ?cs ?p))
		(at start (not (n_station_holding ?cs)))
	)
)

(:durative-action getCapBaseFromCSresourceTask 
	:parameters (?r - robot ?css - cap_station_shelf ?cs - cap_station)
	:duration (= ?duration 10) 
	:condition (and
		(over all (locked ?cs))
		(at start (free ?r))
		(at start (parent_cs ?css ?cs))
		(at start (at ?r ?css))
		(at start (n_holding ?r))
	)
	:effect (and
		(at start (not (free ?r)))
		(at end (free ?r))
		(at end (holding_capbase ?r))
		(at end (not (n_holding ?r)))
	)
)

(:durative-action deliverCapBaseToCSresourceTask 
	:parameters (?r - robot ?csi - cap_station_input ?cs - cap_station)
	:duration (= ?duration 10) 
	:condition (and 
		(over all (locked ?cs))
		(at start (free ?r))
		(at start (parent_cs ?csi ?cs))
		(at start (at ?r ?csi))
		(at start (holding_capbase ?r))	
		(at start (n_cs_station_has_capbase ?cs))	
	)
	:effect (and
		(at start (not (free ?r)))
		(at end (free ?r))
		(at end (n_holding ?r))
		(at end (cs_station_has_capbase ?cs))
		(at end (not (n_cs_station_has_capbase ?cs)))	
	)
)



(:durative-action getProductFromCScriticalTask 
	:parameters (?r - robot ?cso - cap_station_output ?cs - cap_station ?p - product)
	:duration (= ?duration 10) 
	:condition (and 
		(at start (locked ?cs))
		(at start (free ?r))
		(at start (parent_cs ?cso ?cs))
		(at start (at ?r ?cso))
		(at start (n_holding ?r))
		(at start (station_holding ?cs ?p))
	)
	:effect (and
		(at start (not (free ?r)))
		(at end (free ?r))
		(at end (holding ?r ?p))
		(at end (not (n_holding ?r)))
		(at end (stage_c0_2 ?p ))
		(at end (not (stage_c0_1 ?p)))
		(at end (not (station_holding ?cs ?p)))
		(at end (n_station_holding ?cs))
		(at end (n_cs_station_has_capbase ?cs))
		(at end (not (cs_station_has_capbase ?cs)))
		(at end (not (locked ?cs)))
		(at end (n_locked ?cs))
		;(at end (finish ?cs))
	)
)

(:durative-action deliverProductToDScriticalTask 
	:parameters (?r - robot ?ds - delivery_station ?p - product)
	:duration (= ?duration 10) 
	:condition (and 
		(over all (locked ?ds))
		(at start (free ?r))
		(at start (at ?r ?ds))
		(at start (holding ?r ?p))	
		(at start (stage_c0_2 ?p))	
	)
	:effect (and
		(at start (not (free ?r)))
		(at end (free ?r))
		(at end (n_holding ?r))
		(at end (stage_c0_3 ?p ))
		(at end (not (stage_c0_2 ?p)))
		(at end (not (locked ?ds)))
		(at end (n_locked ?ds))
	)
)










)
