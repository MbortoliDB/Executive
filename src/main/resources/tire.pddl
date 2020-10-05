(define (domain tire)
 (:requirements :strips :typing :durative-actions :preferences :constraints :fluents :adl :numeric-fluents )
 (:types tire location)
 (:predicates 
    (at ?t - tire ?l - location)
    (onaxle ?t - tire)
    (nonaxle ?t - tire)
    (isaxle ?l - location)
    (nisaxle ?l - location)
    (empty ?l - location)
 )


(:durative-action move 
	:parameters (?tire - tire ?from ?to - location)
	:duration (= ?duration 5) 
	:condition (and (at start (at ?tire ?from)) (at start (nonaxle ?tire)))
	:effect (and
             (at start (not (at ?tire ?from)))
             (at end (at ?tire ?to))
 	)
)

(:durative-action puton 
	:parameters (?tire - tire ?loc - location)
	:duration (= ?duration 15) 
	:condition (and 
			(at start (at ?tire ?loc))
			(at start (isaxle ?loc))
			(at start (empty ?loc))
		   )
	:effect (and
             (at end (onaxle ?tire))
	     (at end (not (nonaxle ?tire)))
	     (at start (not (empty ?loc)))
 	)
)

(:durative-action putoff 
	:parameters (?tire - tire ?loc - location)
	:duration (= ?duration 15) 
	:condition (and 
			(at start (at ?tire ?loc))
			(at start (onaxle ?tire))
			(at start (isaxle ?loc))
		   )
	:effect (and
             (at end (not (onaxle ?tire)))
	     (at end (nonaxle ?tire))
	     (at end (empty ?loc))
 	)
)


)
