----
insert into master (id, hold_mark, name, balance, unino) values
  ('62610123456', '', '帳號一', 0, 'A123456789'),
  ('69210111111', '', '帳號二', 0, 'A222222222'),
  ('69310000000', '', 'Bin', 0, 'A198765432');
  
----
insert into bank_info (id, bic, corr_flag) values
('PNBPUS3NNYC', 'PNBPUS3NNYC', true),
('PNBPUS33PHL', 'PNBPUS33PHL', true),
('MRMDUS33', 'MRMDUS33', true),
('CITIUS33', 'CITIUS33', true),
('CHASUS33', 'CHASUS33', true),
('BOFAUS6S', 'BOFAUS6S', true),
('BKTRUS33', 'BKTRUS33', true),
('IRVTUS3N', 'IRVTUS3N', false)
;
 
----
insert into swift_message (id, status, msg) values  
('1', 0,
'{1:F01FCBKTWTPA6013193215992}
{2:O1031144200521ROYCCAT2BXXX45332010912005220014N}
{3:{111:001}{121:6df91c4a-3d41-48c4-ac99-05536a5f69d6}} 
{4:
:20:CA200521226481
:23B:CRED
:32A:200522USD1187,
:33B:USD1187,
:50K:/065324003877 TEST 3993 MARK
:52A://CC000300905 ROYCCAT2
:53A:CHASUS33
:54A:IRVTUS3N
:57D:FIRST COMMERCIAL BANK, TAINAN
BRANCH 82 CHUNG YI RD, SECTION 2
TAINAN
:59:/62610123456
TEST CO., LTD.
NO.33.
CITY
TAINAN
:70:PO 45845
:71A:SHA
:71F:USD0,
-}{5:{MAC:00000000}{CHK:04E48C43D6D9}}{S:{SAC:}{COP:P}}
'
)
,
('2', 0,
'
{1:F01FCBKTWTPA6013193215992}
{2:O1031144200521ROYCCAT2BXXX45332010912005220014N}
{3:{111:001}{121:6df91c4a-3d41-48c4-ac99-05536a5f69d6}} 
{4:
:20:CA200521226481
:23B:CRED
:32A:200522USD1187,
:33B:USD1187,
:50K:/065324003877 TEST 3993 MARK
:52A://CC000300905 ROYCCAT2
:53A:CHASUS33
:54A:BKTRUS33
:57D:FIRST COMMERCIAL BANK, TAINAN
BRANCH 82 CHUNG YI RD, SECTION 2
TAINAN
:59:/69210111111
TEST CO., LTD.
NO.33.
CITY
TAINAN
:70:PO 45845
:71A:SHA
:71F:USD0,
-} 
'
),
('3', 0,
'
{1:F01FCBKTWTPA6013193215992}
{2:O1031144200521ROYCCAT2BXXX45332010912005220014N}
{3:{111:001}{121:6df91c4a-3d41-48c4-ac99-05536a5f69d6}} 
{4:
:20:CA200521226481
:23B:CRED
:32A:200522USD1187,
:33B:USD1187,
:50K:/065324003877 TEST 3993 MARK
:52A://CC000300905 ROYCCAT2
:53A:CHASUS33
:54A:BKTRUS33
:57D:FIRST COMMERCIAL BANK, TAINAN
BRANCH 82 CHUNG YI RD, SECTION 2
TAINAN
:59:/69310000000
bin CO.
NO.33.
TAINAN
:70:PO 45845
:71A:SHA
:71F:USD0,
-} 
'
),
('4', 0,
'
{1:F01FCBKTWTPA6013193215992}
{2:O1031144200521ROYCCAT2BXXX45332010912005220014N}
{3:{111:001}{121:6df91c4a-3d41-48c4-ac99-05536a5f69d6}} 
{4:
:20:CA200521226481
:23B:CRED
:32A:200522USD1188,
:33B:USD1188,
:50K:/065324003877 TEST 3993 MARK
:52A://CC000300905 ROYCCAT2
:53A:CHASUS33
:54A:BKTRUS33
:57D:FIRST COMMERCIAL BANK, TAINAN
BRANCH 82 CHUNG YI RD, SECTION 2
TAINAN
:59:/62610123456
ANDY CO.
NO.33.
TAINAN
:70:PO 45845
:71A:SHA
:71F:USD0,
-} 
'
)
;