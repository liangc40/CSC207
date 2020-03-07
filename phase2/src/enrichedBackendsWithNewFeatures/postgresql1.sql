
SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_with_oids = false;

DROP SCHEMA transit_system CASCADE;
CREATE SCHEMA IF NOT EXISTS transit_system;
SET search_path TO transit_system;


CREATE TABLE "user" (
    user_id serial PRIMARY KEY,
    name character varying(50) NOT NULL,
    email character varying(50) UNIQUE NOT NULL,
    password character varying(100) NOT NULL,
    registration_time bigint,
    birthday character varying(50),
    traffic_station_near_home character varying(50),
    traffic_station_near_work character varying(50),
    past_trip_manager_id int,
    user_money_manager_id int,
    card_ids int []
);

CREATE UNIQUE INDEX unique_user_email ON "user" (email);


CREATE TABLE card (
    card_id serial PRIMARY KEY,
    user_id integer REFERENCES "user" (user_id),
    card_money_manager_id integer UNIQUE NOT NULL,
    past_trip_manager_id integer UNIQUE NOT NULL,
    transit_pass_ids integer[],
    is_active boolean NOT NULL,
    registration_time bigint NOT NULL
);

CREATE TABLE card_money_manager (
    card_money_manager_id serial PRIMARY KEY,
    balance double precision NOT NULL,
    automatic_load_amount double precision NOT NULL,
    registration_city VARCHAR(50) NOT NULL,
    card_id integer
);

CREATE TABLE past_trip_manager (
    past_trip_manager_id serial PRIMARY KEY,
    past_trip_ids integer[]
);

CREATE TABLE transit_pass (
    transit_pass_id serial PRIMARY KEY,
    card_id integer NOT NULL,
    city VARCHAR(50),
    registration_date VARCHAR(50),
    expiration_date VARCHAR(50),
    charge_amount double precision
);

CREATE TABLE admin_user (
    admin_user_id serial PRIMARY KEY,
    user_id integer NOT NULL,
    city VARCHAR(50) UNIQUE NOT NULL,
    today_daily_report_id integer,
    current_year integer,
    current_month integer
);

CREATE TABLE daily_reports_stored_by_time (
    id serial PRIMARY KEY,
    admin_user_id integer NOT NULL,
    daily_report_id integer,
    "year" integer,
    "month" integer,
    "day" integer
);

CREATE TABLE transit_pass_expiration_date (
    id serial PRIMARY KEY,
    admin_user_id integer NOT NULL,
    expiration_date VARCHAR(50),
    transit_pass_ids integer[]
);

CREATE TABLE user_money_manager (
    user_money_manager_id serial PRIMARY KEY,
    user_id integer NOT NULL,
    total_traffic_fare double precision DEFAULT 0.0,
    membership_discount_rate double precision,
    status VARCHAR(50) DEFAULT 'Common User',
    child_discount_id integer
);

CREATE TABLE general_trip (
    general_trip_id serial PRIMARY KEY,
    enter_station VARCHAR(50),
    exit_station VARCHAR(50),
    trip_segment_ids int[]
);

CREATE TABLE trip_segment (
    trip_segment_id serial PRIMARY KEY,
    card_id integer,
    enter_time bigint,
    enter_station VARCHAR(50),
    exit_time bigint,
    exit_station VARCHAR(50),
    city VARCHAR(50),
    pay_at_entrance boolean,
    stops_traveled int,
    distance_traveled double precision,
    traffic_mode VARCHAR(50)
);

CREATE TABLE daily_report (
    daily_report_id serial PRIMARY KEY,
    date VARCHAR(50),
    daily_report_money_manager_id integer
);

CREATE TABLE daily_report_money_manager (
    daily_report_money_manager_id serial PRIMARY KEY,
    daily_cost double precision,
    daily_revenue double precision
);

CREATE TABLE total_fare_by_city_table (
    id serial PRIMARY KEY,
    user_id integer NOT NULL,
    city VARCHAR(50) NOT NULL,
    total_fare_by_city double precision
);

CREATE TABLE cap_amount_table (
    id serial PRIMARY KEY,
    city VARCHAR(50) UNIQUE NOT NULL,
    cap_amount double precision
);

INSERT INTO cap_amount_table(id, city, cap_amount) VALUES (1, 'Toronto', 6.0);

CREATE TABLE one_way_trip_frequency_table (
    id serial PRIMARY KEY,
    past_trip_manager_id integer NOT NULL,
    card_id integer NOT NULL,
    route_name VARCHAR(100) NOT NULL,
    times int
);

CREATE TABLE trip_frequency_between_stations_table (
    id serial PRIMARY KEY,
    past_trip_manager_id integer NOT NULL,
    route_name VARCHAR(100) NOT NULL,
    times int
);

CREATE TABLE children_discount (
    children_discount_id serial PRIMARY KEY,
    discount_rate double precision,
    expiration_date VARCHAR(50),
    user_id integer NOT NULL
);

CREATE TABLE node  (
    nid serial PRIMARY KEY,
    name character varying(50),
    city character varying(50)
);

CREATE TABLE city_to_strategy_table  (
--    ctstid serial PRIMARY KEY,
    ctstid serial PRIMARY KEY,
    city VARCHAR(50) UNIQUE NOT NULL,
    charging_strategy VARCHAR(50) NOT NULL
);

INSERT INTO city_to_strategy_table(ctstid, city, charging_strategy) VALUES (1, 'Toronto', 'Cap Strategy');
INSERT INTO city_to_strategy_table(ctstid, city, charging_strategy) VALUES (2, 'Montreal', 'Transfer Strategy');
INSERT INTO city_to_strategy_table(ctstid, city, charging_strategy) VALUES (3, 'Vancouver', 'Non Transfer Strategy');

CREATE TABLE charging_time_table (
    cttid serial PRIMARY KEY,
    city VARCHAR(50) NOT NULL,
    traffic_mode VARCHAR(50) NOT NULL,
    pay_at_entrance boolean NOT NULL
);

INSERT INTO charging_time_table(cttid, city, traffic_mode, pay_at_entrance) VALUES (1, 'Toronto', 'Bus', true);
INSERT INTO charging_time_table(cttid, city, traffic_mode, pay_at_entrance) VALUES (2, 'Toronto', 'Subway', false);
INSERT INTO charging_time_table(cttid, city, traffic_mode, pay_at_entrance) VALUES (3, 'Montreal', 'Bus', true);
INSERT INTO charging_time_table(cttid, city, traffic_mode, pay_at_entrance) VALUES (4, 'Montreal', 'Subway', true);

CREATE TABLE station_to_city_table (
    id serial PRIMARY KEY,
    station VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL
);

INSERT INTO station_to_city_table(id, station, city) VALUES (1, 'St George', 'Toronto');
INSERT INTO station_to_city_table(id, station, city) VALUES (2, 'Museum', 'Toronto');
INSERT INTO station_to_city_table(id, station, city) VALUES (3, 'Queens Park','Toronto');
INSERT INTO station_to_city_table(id, station, city) VALUES (4, 'St Patrick', 'Toronto');

CREATE TABLE pricing_strategy_table (
    id serial PRIMARY KEY,
    city VARCHAR(50) NOT NULL,
    traffic_mode VARCHAR(50) NOT NULL,
    is_revenue boolean,
    pricing_strategy VARCHAR(50)
);

INSERT INTO pricing_strategy_table(id, city, traffic_mode, is_revenue, pricing_strategy) VALUES (1, 'Toronto', 'Bus', true, 'fixed price strategy');
INSERT INTO pricing_strategy_table(id, city, traffic_mode, is_revenue, pricing_strategy) VALUES (2, 'Toronto', 'Bus', false, 'stop strategy');
INSERT INTO pricing_strategy_table(id, city, traffic_mode, is_revenue, pricing_strategy) VALUES (3, 'Toronto', 'Subway', true, 'stop strategy');
INSERT INTO pricing_strategy_table(id, city, traffic_mode, is_revenue, pricing_strategy) VALUES (4, 'Toronto', 'Subway', false, 'stop strategy');

CREATE TABLE strategy_transferable_table (
    id serial PRIMARY KEY,
    strategy_name VARCHAR(50) NOT NULL,
    is_transferable boolean
);

INSERT INTO strategy_transferable_table(id, strategy_name, is_transferable) VALUES (1, 'CapStrategy', true);
INSERT INTO strategy_transferable_table(id, strategy_name, is_transferable) VALUES (2, 'TransferStrategy', true);
INSERT INTO strategy_transferable_table(id, strategy_name, is_transferable) VALUES (3, 'NonTransferStrategy', false);

CREATE TABLE discount_applicable_table (
    dtid serial PRIMARY KEY,
    city VARCHAR(50) UNIQUE NOT NULL,
    discount_mode Boolean[]
);

INSERT INTO discount_applicable_table(dtid, city, discount_mode) VALUES(1, 'Toronto', '{true, true, true, true}');
INSERT INTO discount_applicable_table(dtid, city, discount_mode) VALUES(2, 'Montreal', '{true, false, false, true}');

CREATE TABLE child_discount_table (
    cdtid serial PRIMARY KEY,
    city VARCHAR(50) NOT NULL,
    age_group VARCHAR(50),
    discount_strategy VARCHAR(50)[2]
);

INSERT INTO child_discount_table(cdtid, city, age_group, discount_strategy) VALUES(1, 'Toronto', 'child pass', '{10, 0.5}');
INSERT INTO child_discount_table(cdtid, city, age_group, discount_strategy) VALUES(2, 'Toronto', 'junior pass', '{15, 0.8}');
INSERT INTO child_discount_table(cdtid, city, age_group, discount_strategy) VALUES(3, 'Montreal', 'child pass', '{7, 0}');
INSERT INTO child_discount_table(cdtid, city, age_group, discount_strategy) VALUES(4, 'Montreal', 'junior pass', '{15, 0.7}');

CREATE TABLE membership_discount_table (
    mdid serial PRIMARY KEY,
    city VARCHAR(50)  NOT NULL,
    membership_group VARCHAR(50),
    discount_strategy VARCHAR(50)[2]
);

INSERT INTO membership_discount_table(mdid, city, membership_group, discount_strategy) VALUES(1, 'Toronto', 'Silver', '{5000, 0.95}');
INSERT INTO membership_discount_table(mdid, city, membership_group, discount_strategy) VALUES(2, 'Toronto', 'Gold', '{12000, 0.9}');
INSERT INTO membership_discount_table(mdid, city, membership_group, discount_strategy) VALUES(3, 'Toronto', 'Platinum','{25000, 0.85}');

CREATE TABLE transit_pass_table (
    tptid serial PRIMARY KEY,
    city VARCHAR(50) NOT NULL,
    transit_pass_type VARCHAR(50),
    price VARCHAR(50)
);
INSERT INTO transit_pass_table(tptid, city, transit_pass_type, price) VALUES(1, 'Toronto', 'Day Pass', '15');
INSERT INTO transit_pass_table(tptid, city, transit_pass_type, price) VALUES(2, 'Toronto', 'Three Day Pass', '30');
INSERT INTO transit_pass_table(tptid, city, transit_pass_type, price) VALUES(3, 'Toronto', 'Seven Day Pass', '50');
INSERT INTO transit_pass_table(tptid, city, transit_pass_type, price) VALUES(4, 'Toronto', 'Weekly Pass', '45');
INSERT INTO transit_pass_table(tptid, city, transit_pass_type, price) VALUES(5, 'Toronto', 'Monthly Pass', '120');

CREATE TABLE revenue_cost_table (
    rctid serial PRIMARY KEY,
    city VARCHAR(50) NOT NULL,
    traffic_mode VARCHAR(50),
    revenue_cost VARCHAR(50)[2]
);

INSERT INTO revenue_cost_table(rctid, city, traffic_mode, revenue_cost) VALUES(1, 'Toronto', 'Bus', '{2.0, 0.1}');
INSERT INTO revenue_cost_table(rctid, city, traffic_mode, revenue_cost) VALUES(2, 'Toronto','Subway','{0.5, 0.15}');
INSERT INTO revenue_cost_table(rctid, city, traffic_mode, revenue_cost) VALUES(3, 'Montreal', 'Bus', '{2.0, 0.1}');
INSERT INTO revenue_cost_table(rctid, city, traffic_mode, revenue_cost) VALUES(4, 'Montreal', 'Subway', '{3.0, 0.15}');

CREATE TABLE edge  (
    eid serial PRIMARY KEY,
    "start" integer REFERENCES "node" (nid),
    "stop" integer REFERENCES "node" (nid),
     type character varying(50),
    distance double precision,
    duration int
);

--CREATE TABLE cap_amount_table  (
--    cap_amount_id serial PRIMARY KEY,
--    city VARCHAR(50) UNIQUE NOT NULL,
--    amount double precision
--);

