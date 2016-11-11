open util/ boolean

/* ------- User and car definition --------*/
sig User {}
sig Car {
	position: Position,
	battery: Int
} {
	battery > 0 and battery < 15
}

// Two car cannot share the same position...they would be overlapping!
fact uniquePositions {
	all disj c1, c2: Car | c1.position != c2.position
}
/* -----------------------------------*/

/* --------- System definition -----------*/
one sig System {
	users: set User,
	cars: set Car,
	reservations: set Reservation,
	monitorings: set Monitoring,
	codeRequests: set CodeRequest,
	rides: set Ride,
	safeAreas: set SafeArea
} {
	#cars >0
	#safeAreas > 0
}

// The system contains all of the service objects
fact systemContainsAll {
	all u: User | u in System.users
	all c: Car | c in System.cars
	all r: Reservation | r in System.reservations
    all m: Monitoring | m in System.monitorings
	all c: CodeRequest | c in System.codeRequests
	all r: Ride | r in System.rides
}
/* -----------------------------------*/

/* ----------- Service states ------------*/
sig Reservation {
	user: one User,
	car: one Car
}

fact reservationUniqueness {
	// A user cannot make more than one reservation
	all disj r1, r2: Reservation | r1.user != r2.user
	// A car cannot be reserved by more than one user
	all disj r1, r2: Reservation | r1.car != r2.car
}

sig Monitoring {
	mon_users: set User,
	mon_car: Car
} {
	#mon_users > 0
}

// There are no multiple monitorings on the same car
fact oneMonitorForCar {
	all disj m1, m2: Monitoring | m1.mon_car != m2.mon_car
}

// Monitoring and reservations only on the permitted cars
fact permittedCarsMonitorReservation {
	all m: Monitoring | m.mon_car in System.cars
	all r: Reservation | r.car in System.cars
}

// A user cannot be reserving a car and monitoring others
fact monitorOrReservation {
	all r: Reservation, m: Monitoring | #(r.user & m.mon_users) = 0
}

// Asserts that not every user has made a reservation or is monitoring a car
assert someUserIdle {
	some u: User | no r: Reservation, m: Monitoring | r.user = u and u in m.mon_users
}
/* -----------------------------------*/

/* ---- Code request after a reservation ----*/
sig CodeRequest {
	user: one User,
	accessCode: one Code
}

sig Code{}

// All the codes to access a car are unique
fact codeUniqueness {
	no disj c1, c2: CodeRequest | c1.accessCode = c2.accessCode
}

/* A user can request for an access code only if he/she has already
 made a reservation. Moreover, a user cannot make more than one
 reservation */
fact codeRequestFollowsReservation {
	all c: CodeRequest | one r: Reservation | c.user = r.user 
	all disj c1, c2: CodeRequest | c1.user != c2.user
}

// All the codes of the world are generated only for a code request
fact noUnusedCode {
	#CodeRequest = #Code
}
/* -----------------------------------*/

/* -------------- Ride ----------------*/
sig Ride {
	user: User,
	car: Car,
	cost: Int,
	currentFee: Fee,
	rideDuration: Int,
	possibleBonuses: set Bonus,
	carStopped: Bool
} {
	rideDuration > 0
}

// A user cannot be in more than one ride and so does a car
fact rideSingularity {
	all disj r1, r2: Ride | 
		r1.user != r2.user and 
		r1.car != r2.car
}

// When the user is riding a car, the reservation and monitorings users are cancelled
fact onceRidingReservationAndMonitoringExpire {
	all r: Ride, u: User, c: Car | r.user = u and r.car = c
	implies
	(no res: Reservation | res.user = u or res.car = c)
	  and 
	 (no m: Monitoring | u in m.mon_users or m.car = c)
}

fact rideFeeConstraint {
	all r: Ride | (r.carStopped = True and r.car in SafeArea.parkedCars) implies r.currentFee = SafeAreaFee
	all r: Ride | (r.carStopped = True and r.car not in SafeArea.parkedCars) implies r.currentFee = StopFee
	all r: Ride | r.carStopped = False implies r.currentFee = StandardFee
}

// Even the code request should expire after the ride begins
assert rideImpliesNoCodeRequest {
	all r: Ride | no c: CodeRequest | r.user = c.user	
}
/* -----------------------------------*/

/* --------- Accident description --------*/
sig Accident {
	unfortunateRide: one Ride,
	assignedOperators: set Operator
} {
	#assignedOperators > 0
}

sig Operator{}

// An accident can occur only during a ride
fact noAccidentWithoutRide {
	all a: Accident | a.unfortunateRide.currentFee = SafeAreaFee
}
/* ----------- Fee description ----------*/
abstract sig Fee{
	fee: Int
}

one sig StopFee extends Fee{
	stopMultiplier: Int
}

one sig SafeAreaFee extends Fee {
	safeMultiplier: Int
}

one sig StandardFee extends Fee {
	standardMultiplier: Int
}

/* When stopping and leaving the car in a safe zone, the fee is zero.
	When leaving the car there is a small fee due to the continue use of
	the service. However, this fee is smaller than the one used during a ride
	because there is no battery consumption */
fact feeConstraints {
	StopFee.stopMultiplier > SafeAreaFee.safeMultiplier and
	StandardFee.standardMultiplier > StopFee.stopMultiplier and
	SafeAreaFee.safeMultiplier = 0
}
/* -----------------------------------*/

/* ----- Position and areas description ----*/
sig Position{
	latitude: Int,
	longitude: Int
}

sig SafeArea{
	parkedCars: set Car,
	availablePositions: set Position,
}{	
	#availablePositions > 0
}

sig SpecialSafearea extends SafeArea{
	availableChargers: Int
} {
	availableChargers > 0 && availableChargers < 10
}

// It is a third person/society that owns parking areas and can sell/buy it to/from our society
sig Stakeholder {
	owned_areas: set SafeArea
} {
 	#owned_areas > 0
}

// Safe areas is a set contained within the system and the stakeholders
fact safeAreaDomain {
	no s: SafeArea | s in Stakeholder.owned_areas and s in System.safeAreas
	SafeArea = Stakeholder.owned_areas + System.safeAreas	
}

// A position that is in a safe area cannot appear in another safe area
fact allSafeAreaHaveDisjPositions {
	all s1, s2: SafeArea | s1!=s2 implies #(s1.availablePositions & s2.availablePositions) = 0
}

// The positions of the parked cars in a safe area are some of the positions available in such area
fact parkedCarPositionInSafeAreaPositions {
	all s: SafeArea | s.parkedCars.position in s.availablePositions
}

// There isn't any car parked in the stakeholders areas
fact noServiceInStakeholderAreas {
no c: Car | c.position in Stakeholder.owned_areas.availablePositions

}

// There should not be any position in a stakeholder area, given the previous assumptions
assert noCarInStakeholderSafeAreas {
		no c: Car | c in Stakeholder.owned_areas.parkedCars
}

// Predicates the addition of a safe area to the system, sold by some stakeholder
pred addSafeArea[sa: SafeArea, s,s': System, st,st': Stakeholder] {
	sa in st.owned_areas and sa not in s.safeAreas
	implies
	st'.owned_areas = st.owned_areas - sa and s'.safeAreas = s.safeAreas + sa
}

// Predicates the subtraction of a safe area from the system, bought by some stakeholder
pred delSafeArea[sa: SafeArea, s,s': System, st,st': Stakeholder] {
	sa not in st.owned_areas and sa in s.safeAreas
	implies
	st'.owned_areas = st.owned_areas + sa and s'.safeAreas = s.safeAreas - sa
}

// Predicates the parking of a car in a safe area
pred parkingCar[c: Car, s,s': SafeArea] {
	c not in SafeArea.parkedCars and c.position in s.availablePositions
	implies
	s'.parkedCars = s.parkedCars + c
}

// Predicates the leaving of a car from a parking zone
pred leavingCar[c: Car, s,s': SafeArea] {
	c in s.parkedCars and c.position in s.availablePositions
	implies
	s'.parkedCars = s.parkedCars - c and 
		c not in SafeArea.parkedCars and
		c.position in s'.availablePositions
}
/* -----------------------------------*/

/* ------ Some bonuses description ------*/
abstract sig Bonus {}

one sig HighBattery extends Bonus {
	discount = 10	
}
one sig LowBattery extends Bonus {
	overcharge = 15
}

/* If during a ride a car has more than 10 battery (50%) the user may get a 
	discount if the condition persists. On the other side if during a ride a car
	has less than 5 battery (20%), the user may incur in a 30% overcharge if
	the condition persists */
fact possibleBonusesPerRide {
	all r: Ride | r.car.battery > 10 implies (HighBattery in r.possibleBonuses and #r.possibleBonuses = 1)
	all r: Ride | r.car.battery < 5 implies LowBattery in r.possibleBonuses and #r.possibleBonuses = 1
	all r: Ride | (r.car.battery >= 5 and r.car.battery <= 10) implies #r.possibleBonuses = 0
}

/* A car should not have both bonuses related to the battery percentage */
assert noRideHasBothBatteryBonuses {
	no r: Ride | HighBattery in r.possibleBonuses and LowBattery in r.possibleBonuses
}
/* -----------------------------------*/

/* Utilities constraints.
	- Sum of #Ride and #Reservation < #User
	- Sum of #Ride and #Reservation < #Car
	- #CodeRequest < #Reservation
*/
fact {
	#User = 6
	#Reservation =2
	#CodeRequest = 1
	#Monitoring < 3 && #Monitoring > 0
	#Ride = 2
	#SafeArea = 2
	#Operator < 3
	#Accident = 1
	#Stakeholder = 1
	some r: Reservation | r.car in SafeArea.parkedCars
	some p: Position | p not in SafeArea.availablePositions
	some r: Ride | r.car.position in System.safeAreas.availablePositions
	some r: Ride | LowBattery in r.possibleBonuses
	some c: Car | c.battery < 5
	some c: Car | c.battery > 10
	#System.safeAreas.parkedCars = 2
}

pred show[] {}

//Utility assertion
assert someResOutArea {
	some r: Reservation | r.car.position not in SafeArea.availablePositions
}

// World controls and check
check someResOutArea
check rideImpliesNoCodeRequest
check someUserIdle
check noRideHasBothBatteryBonuses
check noCarInStakeholderSafeAreas
run addSafeArea for 7 but 5 Int
run delSafeArea for 7 but 5 Int
run parkingCar for 7 but 5 Int
run leavingCar for 7 but 5 Int
run show for 7 but 5 Int
