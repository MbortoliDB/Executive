(define (problem task)
(:domain tire)
(:objects
    axle1 axle2 axle3 axle4 trunck - location
    tire1 tire2 tire3 tire4 sparetire - tire
)
(:init
    (at tire1 axle1)
    (at tire2 axle2)
    (at tire3 axle3)
    (at tire4 axle4)
    (at sparetire trunck)
    (onaxle tire1)
    (onaxle tire2)
    (onaxle tire3)
    (onaxle tire4)
    (nonaxle sparetire)
    (isaxle axle1)
    (isaxle axle2)
    (isaxle axle3)
    (isaxle axle4)
    (nisaxle trunck)


)
(:goal (and
    (at tire2 trunck)
    (at sparetire axle2)
    (onaxle sparetire)
)
)

(:metric minimize (total-time))

)
