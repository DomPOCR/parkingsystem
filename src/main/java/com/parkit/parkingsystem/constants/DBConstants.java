package com.parkit.parkingsystem.constants;

public class DBConstants {

	public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
	public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

	public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
	public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
	public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";

	// select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE
	// from ticket t, parking p where p.parking_number = t.parking_number and
	// t.VEHICLE_REG_NUMBER=? order by t.IN_TIME DESC limit 1

	// DP Cette requête renvoie 0 si le client n'a aucun enregistrement dans la
	// table Ticket
	// renvoie un nombre <> 0 donc TRUE

	// On élimine les durée inférieures à 30 mn

	public static final String IS_REGULAR_CUSTOMER = "select count(t.ID)>0 from ticket t where t.VEHICLE_REG_NUMBER=? and t.PRICE>0";
}
