--
-- PostgreSQL database dump
--

-- Dumped from database version 10.4
-- Dumped by pg_dump version 10.4

-- Started on 2018-07-27 18:32:56 EDT

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

--
-- TOC entry 198 (class 1259 OID 16395)
-- Name: user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "user" (
    uid serial PRIMARY KEY,
    name character varying(50),
    email character varying(50),
    isAdmin boolean DEFAULT false,
    password character varying(100),
    createAt bigint
);

--
-- TOC entry 200 (class 1259 OID 16403)
-- Name: card; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE card (
    cid serial PRIMARY KEY,
    uid integer REFERENCES "user" (uid),
    isactive boolean DEFAULT true,
    deleted boolean DEFAULT false,
    balance double precision,
    createAt bigint
);

CREATE UNIQUE INDEX unique_user_email ON "user" (email);

CREATE TABLE node  (
    nid serial PRIMARY KEY,
    name character varying(50),
    transit_type int
);

CREATE TABLE edge  (
    eid serial PRIMARY KEY,
    "start" integer REFERENCES "node" (nid),
    "stop" integer REFERENCES "node" (nid),
     edge character varying(50),
    distance double precision,
    duration int
);

CREATE TABLE generaltrip  (
    gtid serial PRIMARY KEY,
    uid integer REFERENCES "user" (uid),
--    "start" integer REFERENCES "node" (nid),
--    "stop" integer REFERENCES "node" (nid),
--    "start" character varying(50),
--    "stop" character varying(50),
--    createAt bigint,
    startTime character varying(50),
 --   endTime character varying(50),
--    totalFare double precision,
    finished boolean DEFAULT false
 --   cid integer REFERENCES "card" (cid)

);

--CREATE TABLE tripsegment  (
 --   tsid serial PRIMARY KEY,
--    uid integer REFERENCES "user" (uid),
 --   gtid integer REFERENCES "generaltrip" (gtid),
 --   createAt bigint,
 --   startTime character varying(50),
 --   endTime character varying(50),
 --   "start" integer REFERENCES "node" (nid),
 --   "stop" integer REFERENCES "node" (nid),
 --   type character varying(50),
 --   duration int,
--    fare double precision
--);

CREATE TABLE tripsegment  (
    tsid serial PRIMARY KEY,
    uid integer REFERENCES "user" (uid),
    gtid integer REFERENCES "generaltrip" (gtid),
    cid integer REFERENCES "card" (cid),
 --   createAt bigint,
    startTime character varying(50),
    endTime character varying(50),
    "start" character varying(50),
    "stop" character varying(50),
    transit_line character varying(50),
    fare double precision,
    cap double precision
);

CREATE TABLE Station  (
    sid serial PRIMARY KEY,
    point int,
    next_distance double precision,
    stationname character varying(50),
    line character varying(50)

);



COPY transit_system.node (nid, name, transit_type) FROM stdin;
1	a	\N
2	b	\N
3	c	\N
4	d	\N
5	e	\N
\.




COPY transit_system.edge (eid, start, stop, edge, distance, duration) FROM stdin;
1	1	2	\N	3	\N
2	2	3	\N	5	\N
3	2	4	\N	6	\N
4	3	5	\N	8	\N
5	4	5	\N	8	\N
6	2	5	\N	2	\N
\.

COPY transit_system.station (sid, point, next_distance, stationname, line) FROM stdin;
1	1	2	Kipling	Subway Line 1
2	2	3	Jane	Subway Line 1
3	3	4	Highpark	Subway Line 1
4	4	2	Keele	Subway Line 1
5	5	4	Dundas West	Subway Line 1
6	6	5	Bathurst	Subway Line 1
7	7	4	St George	Subway Line 1
8	8	1	Bay	Subway Line 1
9	9	1	Yonge-Bloor	Subway Line 1
10	10	7	Pape	Subway Line 1
11	11	8	Woodbine	Subway Line 1
12	12	2	Main Street	Subway Line 1
13	13	5	Victoria Park	Subway Line 1
14	14	7	Kennedy	Subway Line 1
15	1	7	Downsview	Subway Line 2
16	2	6	Yorkdale	Subway Line 2
17	3	5	Eglinton West	Subway Line 2
18	4	7	Dupont	Subway Line 2
19	5	7	St George	Subway Line 2
20	6	2	Queens Park	Subway Line 2
21	7	8	UnioStaion	Subway Line 2
22	8	5	Dundas	Subway Line 2
23	9	4	Yonge-Bloor	Subway Line 2
24	10	4	StClaire	Subway Line 2
25	11	7	Eglinton	Subway Line 2
26	12	5	YorkMills	Subway Line 2
27	13	4	Yonge-Sheppard	Subway Line 2
28	14	1	North York	Subway Line 2
29	15	2	Finch	Subway Line 2
30	1	6	Finch	Bus Line 1
31	2	7	Don Mills	Bus Line 1
32	3	11	Warden	Bus Line 1
33	4	9	Midland	Bus Line 1
34	1	7	Kennedy	Bus Line 2
35	2	7	Lawrence East	Bus Line 2
36	3	7	Ellsmere	Bus Line 2
37	4	7	Midland	Bus Line 2
\.

SELECT pg_catalog.setval('node_nid_seq', 5, true);
SELECT pg_catalog.setval('edge_eid_seq', 6, true);
SELECT pg_catalog.setval('station_sid_seq', 37, true);

