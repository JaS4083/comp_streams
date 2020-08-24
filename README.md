# comp_streams

We held a competition and have logs from finish and start with ids of participants and their time of starting and finishing accordingly. 
1.Using Parsing Logic need to extract information from tag_read_start. If tags are repeated, need to use first occurrence.
2.Using Parsing Logic need to extract information from tag_read_finish. If tags are repeated, need to use last occurrence. 
3.Need to show in console to participants with lowest time in this competition.


TagRead example:

aaE4058001c6df0700011608131047512800FS

----058001c6df07----160813104751------



//Index start from 0

*TAG_STARTS_AT = 4;*

*TAG_ENDS_AT = 16;* //exclusive

*TIMESTAMP_STARTS_AT = 20;*

TIMESTAMP_ENDS_AT = 32; //exclusive 

Timestamp parsing pattern: "yyMMddHHmmss" 

