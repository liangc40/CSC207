--
-- PostgreSQL database dump
--

-- Dumped from database version 10.4
-- Dumped by pg_dump version 10.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: transit_system; Type: SCHEMA; Schema: -; Owner: GGG
--

CREATE SCHEMA transit_system;


ALTER SCHEMA transit_system OWNER TO "GGG";

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: card; Type: TABLE; Schema: transit_system; Owner: GGG
--

CREATE TABLE transit_system.card (
    cid integer NOT NULL,
    uid integer,
    isactive boolean,
    balance double precision,
    createat bigint
);


ALTER TABLE transit_system.card OWNER TO "GGG";

--
-- Name: card_cid_seq; Type: SEQUENCE; Schema: transit_system; Owner: GGG
--

CREATE SEQUENCE transit_system.card_cid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE transit_system.card_cid_seq OWNER TO "GGG";

--
-- Name: card_cid_seq; Type: SEQUENCE OWNED BY; Schema: transit_system; Owner: GGG
--

ALTER SEQUENCE transit_system.card_cid_seq OWNED BY transit_system.card.cid;


--
-- Name: edge; Type: TABLE; Schema: transit_system; Owner: GGG
--

CREATE TABLE transit_system.edge (
    eid integer NOT NULL,
    start integer,
    stop integer,
    distance double precision,
    duration integer
);


ALTER TABLE transit_system.edge OWNER TO "GGG";

--
-- Name: edge_eid_seq; Type: SEQUENCE; Schema: transit_system; Owner: GGG
--

CREATE SEQUENCE transit_system.edge_eid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE transit_system.edge_eid_seq OWNER TO "GGG";

--
-- Name: edge_eid_seq; Type: SEQUENCE OWNED BY; Schema: transit_system; Owner: GGG
--

ALTER SEQUENCE transit_system.edge_eid_seq OWNED BY transit_system.edge.eid;


--
-- Name: generaltrip; Type: TABLE; Schema: transit_system; Owner: GGG
--

CREATE TABLE transit_system.generaltrip (
    gtid integer NOT NULL,
    uid integer,
    start integer,
    stop integer,
    createat bigint,
    cid integer
);


ALTER TABLE transit_system.generaltrip OWNER TO "GGG";

--
-- Name: generaltrip_gtid_seq; Type: SEQUENCE; Schema: transit_system; Owner: GGG
--

CREATE SEQUENCE transit_system.generaltrip_gtid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE transit_system.generaltrip_gtid_seq OWNER TO "GGG";

--
-- Name: generaltrip_gtid_seq; Type: SEQUENCE OWNED BY; Schema: transit_system; Owner: GGG
--

ALTER SEQUENCE transit_system.generaltrip_gtid_seq OWNED BY transit_system.generaltrip.gtid;


--
-- Name: node; Type: TABLE; Schema: transit_system; Owner: GGG
--

CREATE TABLE transit_system.node (
    nid integer NOT NULL,
    name character varying(50),
    type character varying(50),
    city character varying(50)
);


ALTER TABLE transit_system.node OWNER TO "GGG";

--
-- Name: node_nid_seq; Type: SEQUENCE; Schema: transit_system; Owner: GGG
--

CREATE SEQUENCE transit_system.node_nid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE transit_system.node_nid_seq OWNER TO "GGG";

--
-- Name: node_nid_seq; Type: SEQUENCE OWNED BY; Schema: transit_system; Owner: GGG
--

ALTER SEQUENCE transit_system.node_nid_seq OWNED BY transit_system.node.nid;


--
-- Name: tripsegment; Type: TABLE; Schema: transit_system; Owner: GGG
--

CREATE TABLE transit_system.tripsegment (
    tsid integer NOT NULL,
    uid integer,
    gtid integer,
    createat bigint,
    start integer,
    stop integer,
    duration integer,
    fare double precision
);


ALTER TABLE transit_system.tripsegment OWNER TO "GGG";

--
-- Name: tripsegment_tsid_seq; Type: SEQUENCE; Schema: transit_system; Owner: GGG
--

CREATE SEQUENCE transit_system.tripsegment_tsid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE transit_system.tripsegment_tsid_seq OWNER TO "GGG";

--
-- Name: tripsegment_tsid_seq; Type: SEQUENCE OWNED BY; Schema: transit_system; Owner: GGG
--

ALTER SEQUENCE transit_system.tripsegment_tsid_seq OWNED BY transit_system.tripsegment.tsid;


--
-- Name: user; Type: TABLE; Schema: transit_system; Owner: GGG
--

CREATE TABLE transit_system."user" (
    uid integer NOT NULL,
    name character varying(50),
    email character varying(50),
    isadmin boolean DEFAULT false,
    password character varying(100),
    createat bigint,
    city character varying(50),
    birthday bigint
);


ALTER TABLE transit_system."user" OWNER TO "GGG";

--
-- Name: user_uid_seq; Type: SEQUENCE; Schema: transit_system; Owner: GGG
--

CREATE SEQUENCE transit_system.user_uid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE transit_system.user_uid_seq OWNER TO "GGG";

--
-- Name: user_uid_seq; Type: SEQUENCE OWNED BY; Schema: transit_system; Owner: GGG
--

ALTER SEQUENCE transit_system.user_uid_seq OWNED BY transit_system."user".uid;


--
-- Name: card cid; Type: DEFAULT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.card ALTER COLUMN cid SET DEFAULT nextval('transit_system.card_cid_seq'::regclass);


--
-- Name: edge eid; Type: DEFAULT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.edge ALTER COLUMN eid SET DEFAULT nextval('transit_system.edge_eid_seq'::regclass);


--
-- Name: generaltrip gtid; Type: DEFAULT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.generaltrip ALTER COLUMN gtid SET DEFAULT nextval('transit_system.generaltrip_gtid_seq'::regclass);


--
-- Name: node nid; Type: DEFAULT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.node ALTER COLUMN nid SET DEFAULT nextval('transit_system.node_nid_seq'::regclass);


--
-- Name: tripsegment tsid; Type: DEFAULT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.tripsegment ALTER COLUMN tsid SET DEFAULT nextval('transit_system.tripsegment_tsid_seq'::regclass);


--
-- Name: user uid; Type: DEFAULT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system."user" ALTER COLUMN uid SET DEFAULT nextval('transit_system.user_uid_seq'::regclass);


--
-- Data for Name: card; Type: TABLE DATA; Schema: transit_system; Owner: GGG
--

COPY transit_system.card (cid, uid, isactive, balance, createat) FROM stdin;
37	80	f	19	1533051586
38	80	f	19	1533051586
36	80	f	39	1533051586
40	81	f	19	1533056337
41	81	f	19	1533056337
39	81	f	39	1533056337
43	82	f	19	1533063363
44	82	f	19	1533063363
42	82	f	39	1533063363
46	83	f	19	1533066623
47	83	f	19	1533066623
45	83	f	39	1533066623
48	65	f	19	1533080256
49	65	f	19	1533080324
50	65	f	19	1533080561
51	65	f	19	1533080593
52	65	f	19	1533080669
53	65	f	19	1533081175
54	65	f	19	1533081227
55	65	f	19	1533081820
56	65	f	19	1533081864
57	65	f	19	1533084355
58	65	f	19	1533084393
59	65	f	19	1533084743
60	65	f	19	1533085039
61	65	f	19	1533085134
62	65	f	19	1533085296
63	65	f	19	1533085301
64	65	f	19	1533085366
65	65	f	19	1533085389
66	65	f	19	1533085583
67	65	f	19	1533085588
68	65	f	19	1533085597
69	65	f	19	1533085635
\.


--
-- Data for Name: edge; Type: TABLE DATA; Schema: transit_system; Owner: GGG
--

COPY transit_system.edge (eid, start, stop, distance, duration) FROM stdin;
\.


--
-- Data for Name: generaltrip; Type: TABLE DATA; Schema: transit_system; Owner: GGG
--

COPY transit_system.generaltrip (gtid, uid, start, stop, createat, cid) FROM stdin;
\.


--
-- Data for Name: node; Type: TABLE DATA; Schema: transit_system; Owner: GGG
--

COPY transit_system.node (nid, name, type, city) FROM stdin;
\.


--
-- Data for Name: tripsegment; Type: TABLE DATA; Schema: transit_system; Owner: GGG
--

COPY transit_system.tripsegment (tsid, uid, gtid, createat, start, stop, duration, fare) FROM stdin;
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: transit_system; Owner: GGG
--

COPY transit_system."user" (uid, name, email, isadmin, password, createat, city, birthday) FROM stdin;
65	Yiming Mo	#	f	123asd	1533012178	\N	\N
80	Li Tai	litai@gmail.com	f	12345	1533051586	\N	\N
81	Li Ta	lita@gmail.com	f	12345	1533056337	\N	\N
82	L Tay	ltay@gmail.com	f	123456	1533063363	\N	\N
83	L Tiay	litay@gmail.com	f	123456	1533066623	\N	\N
85	1	lihuateng@gmail.com	f	2	1533087107	\N	\N
46	Li Hua	lihua@gmail.com	f	12345	1533001231	\N	\N
49	1	1	f	1	1533002186	\N	\N
\.


--
-- Name: card_cid_seq; Type: SEQUENCE SET; Schema: transit_system; Owner: GGG
--

SELECT pg_catalog.setval('transit_system.card_cid_seq', 69, true);


--
-- Name: edge_eid_seq; Type: SEQUENCE SET; Schema: transit_system; Owner: GGG
--

SELECT pg_catalog.setval('transit_system.edge_eid_seq', 1, false);


--
-- Name: generaltrip_gtid_seq; Type: SEQUENCE SET; Schema: transit_system; Owner: GGG
--

SELECT pg_catalog.setval('transit_system.generaltrip_gtid_seq', 1, false);


--
-- Name: node_nid_seq; Type: SEQUENCE SET; Schema: transit_system; Owner: GGG
--

SELECT pg_catalog.setval('transit_system.node_nid_seq', 1, false);


--
-- Name: tripsegment_tsid_seq; Type: SEQUENCE SET; Schema: transit_system; Owner: GGG
--

SELECT pg_catalog.setval('transit_system.tripsegment_tsid_seq', 1, false);


--
-- Name: user_uid_seq; Type: SEQUENCE SET; Schema: transit_system; Owner: GGG
--

SELECT pg_catalog.setval('transit_system.user_uid_seq', 85, true);


--
-- Name: card card_pkey; Type: CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.card
    ADD CONSTRAINT card_pkey PRIMARY KEY (cid);


--
-- Name: edge edge_pkey; Type: CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.edge
    ADD CONSTRAINT edge_pkey PRIMARY KEY (eid);


--
-- Name: generaltrip generaltrip_pkey; Type: CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.generaltrip
    ADD CONSTRAINT generaltrip_pkey PRIMARY KEY (gtid);


--
-- Name: node node_pkey; Type: CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.node
    ADD CONSTRAINT node_pkey PRIMARY KEY (nid);


--
-- Name: tripsegment tripsegment_pkey; Type: CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.tripsegment
    ADD CONSTRAINT tripsegment_pkey PRIMARY KEY (tsid);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (uid);


--
-- Name: unique_user_email; Type: INDEX; Schema: transit_system; Owner: GGG
--

CREATE UNIQUE INDEX unique_user_email ON transit_system."user" USING btree (email);


--
-- Name: card card_uid_fkey; Type: FK CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.card
    ADD CONSTRAINT card_uid_fkey FOREIGN KEY (uid) REFERENCES transit_system."user"(uid);


--
-- Name: edge edge_start_fkey; Type: FK CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.edge
    ADD CONSTRAINT edge_start_fkey FOREIGN KEY (start) REFERENCES transit_system.node(nid);


--
-- Name: edge edge_stop_fkey; Type: FK CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.edge
    ADD CONSTRAINT edge_stop_fkey FOREIGN KEY (stop) REFERENCES transit_system.node(nid);


--
-- Name: generaltrip generaltrip_cid_fkey; Type: FK CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.generaltrip
    ADD CONSTRAINT generaltrip_cid_fkey FOREIGN KEY (cid) REFERENCES transit_system.card(cid);


--
-- Name: generaltrip generaltrip_start_fkey; Type: FK CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.generaltrip
    ADD CONSTRAINT generaltrip_start_fkey FOREIGN KEY (start) REFERENCES transit_system.node(nid);


--
-- Name: generaltrip generaltrip_stop_fkey; Type: FK CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.generaltrip
    ADD CONSTRAINT generaltrip_stop_fkey FOREIGN KEY (stop) REFERENCES transit_system.node(nid);


--
-- Name: generaltrip generaltrip_uid_fkey; Type: FK CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.generaltrip
    ADD CONSTRAINT generaltrip_uid_fkey FOREIGN KEY (uid) REFERENCES transit_system."user"(uid);


--
-- Name: tripsegment tripsegment_gtid_fkey; Type: FK CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.tripsegment
    ADD CONSTRAINT tripsegment_gtid_fkey FOREIGN KEY (gtid) REFERENCES transit_system.generaltrip(gtid);


--
-- Name: tripsegment tripsegment_start_fkey; Type: FK CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.tripsegment
    ADD CONSTRAINT tripsegment_start_fkey FOREIGN KEY (start) REFERENCES transit_system.node(nid);


--
-- Name: tripsegment tripsegment_stop_fkey; Type: FK CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.tripsegment
    ADD CONSTRAINT tripsegment_stop_fkey FOREIGN KEY (stop) REFERENCES transit_system.node(nid);


--
-- Name: tripsegment tripsegment_uid_fkey; Type: FK CONSTRAINT; Schema: transit_system; Owner: GGG
--

ALTER TABLE ONLY transit_system.tripsegment
    ADD CONSTRAINT tripsegment_uid_fkey FOREIGN KEY (uid) REFERENCES transit_system."user"(uid);


--
-- PostgreSQL database dump complete
--

