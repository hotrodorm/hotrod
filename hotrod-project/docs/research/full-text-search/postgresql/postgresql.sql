-- Notes:
-- The extra column searchable_content is not required, but recommended
-- The extra column needs to be updates when the content is updated. Usually:
--   a) Using a trigger on every row update.
--   b) Using an hourly/daily process -- searches won't be precise until next hour/day.

-- 1. Create & populate the table

create table paragraph (
  id int primary key not null,
  content varchar(2000),
  searchable_content tsvector -- this extra column will store a lexemes.
  );

insert into paragraph (id, content) values (1, '        output row1 || row2 if the on/where conditions are true');
insert into paragraph (id, content) values (2, '    for each row2 in table’2');
insert into paragraph (id, content) values (3, '    Hash Join: hashing one table and then perform a scan on the other.');
insert into paragraph (id, content) values (4, '    Merge Join: joining tables "side by side".');
insert into paragraph (id, content) values (5, '    N-Ary Join: similar to NLJ but with more than two tables at once.');
insert into paragraph (id, content) values (6, '    Nested Loop Join "NLJ": this is the one you mention.');
insert into paragraph (id, content) values (7, '->n{s=(1..n).reduce(:*).to_s;s.size-s.reverse.to_i.to_s.size}');
insert into paragraph (id, content) values (8, '->n{s=n.to_s(5).chars;(0...s.size).reduce{|a,b|a+(s[0,b]*'''').to_i(5)}}');
insert into paragraph (id, content) values (9, '5/d   # divide by 5, and leave a copy behind');
insert into paragraph (id, content) values (10, 'Are German, Japanese, and Chinese companies not able to build aircraft jet engines on their own?');
insert into paragraph (id, content) values (11, 'As a bonus, this code is much faster than that which visits each 1..n (and much, much faster than computing the factorial).');
insert into paragraph (id, content) values (12, 'As an example (again, with thanks to Kenny Lau), consider 358 (2413 in base 5) minus its base 5 digits.');
insert into paragraph (id, content) values (13, 'Basic C++ Circle Class');
insert into paragraph (id, content) values (14, 'Calculate Phi (not Pi)');
insert into paragraph (id, content) values (15, 'Calculate the number of trailing zeros in n!');
insert into paragraph (id, content) values (16, 'Can one draw their commander to the command zone, and would that be a draw?');
insert into paragraph (id, content) values (17, 'd0<   # still greater than zero?');
insert into paragraph (id, content) values (18, 'Depending on the size of the tables, column statistics, selectivity of your filter (where) your database can use one or the other. It can even change over time if column statistics & value distributions change.');
insert into paragraph (id, content) values (19, 'Divide 348 by 4 and you get f(358) = 87.');
insert into paragraph (id, content) values (20, 'Edit: Turns out you can save two bytes by mapping to_i before you reduce. Weird :P');
insert into paragraph (id, content) values (21, 'EXPLAIN <sql>');
insert into paragraph (id, content) values (22, 'f+    # if so, apply f to the new value and add');
insert into paragraph (id, content) values (23, 'Facial recognition - Given the context, should the user be made aware of the process?');
insert into paragraph (id, content) values (24, 'for each row1 in table1');
insert into paragraph (id, content) values (25, 'For years, I understood that when tables are joined, one row from primary table is joined to a row in target table after applying conditions I.e the query results will <= rows in the primary table. But I have seen where one row in primary table can be joined multiple times of the conditions allow. e.g the query below''s count function would not work without duplicate rows form primary table ');
insert into paragraph (id, content) values (26, 'Hilbert Primes Golf');
insert into paragraph (id, content) values (27, 'Hollow out an array');
insert into paragraph (id, content) values (28, 'How joins are implemented is actually not important. SQL is a descriptive language, not a procedural language. The query engine can decide the "how". The query is describing the "what".');
insert into paragraph (id, content) values (29, 'How to address a guy talking toxic about women');
insert into paragraph (id, content) values (30, 'How to modify static resources inside folders?');
insert into paragraph (id, content) values (31, 'How to statistically prove if a column has categorical data or not using Python');
insert into paragraph (id, content) values (32, 'How would disabling IPv6 make a server any more secure?');
insert into paragraph (id, content) values (33, 'How would failing to avoid an avalanche play out in Dungeon World?');
insert into paragraph (id, content) values (34, 'If you need to find out why a SQL is slow or unresponsive you''ll need to understand how the database works under the hood. There are multiple strategies databases use to JOIN tables. Commonly (not a complete list):');
insert into paragraph (id, content) values (35, 'If you want to learn what those strategies are, and when each one is convenient, you''ll can start using');
insert into paragraph (id, content) values (36, 'Introduce Your Language!');
insert into paragraph (id, content) values (37, 'Is It Okay If I Wrote A Story Based On True Historical Events?');
insert into paragraph (id, content) values (38, 'Knowing when the code is good enough');
insert into paragraph (id, content) values (39, 'labels of nodes with drop shadow');
insert into paragraph (id, content) values (40, 'Last non-zero digit of n!');
insert into paragraph (id, content) values (41, 'Most people don''t think in terms of Cartesian products. A nested loop is equivalent. The logic is something like this:');
insert into paragraph (id, content) values (42, 'My birthday is uncommon');
insert into paragraph (id, content) values (43, 'My PhD supervisor cannot promise me date to defend my dissertation before my visa expires. Should I just quit my PhD and start over?');
insert into paragraph (id, content) values (44, 'My two cents. I agree that "how" is not important since SQL is a descriptive language. Well... it''s not important until your queries become slow as hell (my experience) when the system is successful and the database grows (a lot).');
insert into paragraph (id, content) values (45, 'Ohm''s law doesn''t seem to be working in this pump');
insert into paragraph (id, content) values (46, 'Outer joins extend this concept, allowing rows from one or both tables to be in the result set even when the on/where conditions are not true.');
insert into paragraph (id, content) values (47, 'Pen Pineapple Apple Pen!');
insert into paragraph (id, content) values (48, 'Print all alphanumeric characters plus underscore');
insert into paragraph (id, content) values (49, 'Product over a range');
insert into paragraph (id, content) values (50, 'Punctuation usage?');
insert into paragraph (id, content) values (51, 'shareimprove this answer');
insert into paragraph (id, content) values (52, 'Spin Echo Diagram, Simplification and Scalability');
insert into paragraph (id, content) values (53, 'States and Capitals');
insert into paragraph (id, content) values (54, 'Subsequence Substitution');
insert into paragraph (id, content) values (55, 'Take Circle Line to Bank');
insert into paragraph (id, content) values (56, 'Take Circle Line to Victoria');
insert into paragraph (id, content) values (57, 'Take District Line to Bank');
insert into paragraph (id, content) values (58, 'Take District Line to Bank');
insert into paragraph (id, content) values (59, 'Take District Line to Bank');
insert into paragraph (id, content) values (60, 'Take District Line to Bank');
insert into paragraph (id, content) values (61, 'Take District Line to Bank');
insert into paragraph (id, content) values (62, 'Take District Line to Bank');
insert into paragraph (id, content) values (63, 'Take District Line to Bank');
insert into paragraph (id, content) values (64, 'Take District Line to Bank');
insert into paragraph (id, content) values (65, 'Take District Line to Bank');
insert into paragraph (id, content) values (66, 'Take District Line to Bank');
insert into paragraph (id, content) values (67, 'Take District Line to Becontree');
insert into paragraph (id, content) values (68, 'Take District Line to Becontree');
insert into paragraph (id, content) values (69, 'Take District Line to Becontree');
insert into paragraph (id, content) values (70, 'Take District Line to Becontree');
insert into paragraph (id, content) values (71, 'Take District Line to Becontree');
insert into paragraph (id, content) values (72, 'Take District Line to Becontree');
insert into paragraph (id, content) values (73, 'Take District Line to Becontree');
insert into paragraph (id, content) values (74, 'Take District Line to Becontree');
insert into paragraph (id, content) values (75, 'Take District Line to Becontree');
insert into paragraph (id, content) values (76, 'Take District Line to Blackfriars');
insert into paragraph (id, content) values (77, 'Take District Line to Blackfriars');
insert into paragraph (id, content) values (78, 'Take District Line to Blackfriars');
insert into paragraph (id, content) values (79, 'Take District Line to Blackfriars');
insert into paragraph (id, content) values (80, 'Take District Line to Cannon Street');
insert into paragraph (id, content) values (81, 'Take District Line to Cannon Street');
insert into paragraph (id, content) values (82, 'Take District Line to Cannon Street');
insert into paragraph (id, content) values (83, 'Take District Line to Cannon Street');
insert into paragraph (id, content) values (84, 'Take District Line to Hammersmith');
insert into paragraph (id, content) values (85, 'Take District Line to Hammersmith');
insert into paragraph (id, content) values (86, 'Take District Line to Hammersmith');
insert into paragraph (id, content) values (87, 'Take District Line to Hammersmith');
insert into paragraph (id, content) values (88, 'Take District Line to Hammersmith');
insert into paragraph (id, content) values (89, 'Take District Line to Hammersmith');
insert into paragraph (id, content) values (90, 'Take District Line to Hammersmith');
insert into paragraph (id, content) values (91, 'Take District Line to Hammersmith');
insert into paragraph (id, content) values (92, 'Take District Line to Notting Hill Gate');
insert into paragraph (id, content) values (93, 'Take District Line to Notting Hill Gate');
insert into paragraph (id, content) values (94, 'Take District Line to Parsons Green');
insert into paragraph (id, content) values (95, 'Take District Line to Temple');
insert into paragraph (id, content) values (96, 'Take District Line to Turnham Green');
insert into paragraph (id, content) values (97, 'Take District Line to Turnham Green');
insert into paragraph (id, content) values (98, 'Take District Line to Upminster');
insert into paragraph (id, content) values (99, 'Take District Line to Upminster');
insert into paragraph (id, content) values (100, 'Take District Line to Upminster');
insert into paragraph (id, content) values (101, 'Take District Line to Upminster');
insert into paragraph (id, content) values (102, 'Take District Line to Upminster');
insert into paragraph (id, content) values (103, 'Take District Line to Upminster');
insert into paragraph (id, content) values (104, 'Take District Line to Upminster');
insert into paragraph (id, content) values (105, 'Take District Line to Upminster');
insert into paragraph (id, content) values (106, 'Take District Line to Upminster');
insert into paragraph (id, content) values (107, 'Take District Line to Upminster');
insert into paragraph (id, content) values (108, 'Take Northern Line to Angel');
insert into paragraph (id, content) values (109, 'Take Northern Line to Bank');
insert into paragraph (id, content) values (110, 'Take Northern Line to Bank');
insert into paragraph (id, content) values (111, 'Take Northern Line to Bank');
insert into paragraph (id, content) values (112, 'Take Northern Line to Mornington Crescent');
insert into paragraph (id, content) values (113, 'Take Victoria Line to Seven Sisters');
insert into paragraph (id, content) values (114, 'Take Victoria Line to Victoria');
insert into paragraph (id, content) values (115, 'Take Victoria Line to Victoria');
insert into paragraph (id, content) values (116, 'The conceptual definition of an inner join is rather simple. It is the Cartesian product of two sets that meets the conditions of the on and where clauses.');
insert into paragraph (id, content) values (117, 'The golfing is a bit of a pain though, with converting from Integer to String to Array, grabbing part of the Array and converting that to String to Integer again for the reduce. Any golfing suggestions are welcome.');
insert into paragraph (id, content) values (118, 'The number of trailing zeros is equal to the number of fives that make up the factorial. Of all the 1..n, one-fifth of them contribute a five, so we start with n/5. Of these n/5, a fifth are multiples of 25, so contribute an extra five, and so on. We end up with f(n) = n/5 + n/25 + n/125 + ..., which is f(n) = n/5 + f(n/5). We do need to terminate the recursion when n reaches zero; also we take advantage of the sequence point at ?: to divide n before the addition.');
insert into paragraph (id, content) values (119, 'There is no concept whatsoever that about "query results will <= rows in the primary table." With some data structures -- notably a fact table with dimension tables joined in -- you will get that behavior. However, that is because the data model is designed for this purpose, not because SQL works that way.');
insert into paragraph (id, content) values (120, 'This a variation of the "How many 5s are there in the prime factorization of n!?" trick that uses Ruby''s simple base conversion builtins.');
insert into paragraph (id, content) values (121, 'This defines a function f which consumes its input from top of stack, and leaves its output at top of stack. See my C answer for the mathematical basis. We repeatedly divide by 5, accumulating the values on the stack, then add all the results:');
insert into paragraph (id, content) values (122, 'This function calculates n! then subtracts the size of n! from the size of (n!).reverse.to_i.to_s, which removes all the zeroes, thus, returning the size of the zeroes themselves.');
insert into paragraph (id, content) values (123, 'This function subtracts the sum of n''s base 5 digits from n and then divides that result by 4. This is related to the sum of the geometric series 1+5+25+..+5**n = (5**n+1)/4.');
insert into paragraph (id, content) values (124, 'This is an anonymous function that accepts any signed integer type and returns an integer. To call it, assign it to a variable. The larger test cases require passing n as a larger type, such as a BigInt.');
insert into paragraph (id, content) values (125, 'Tips for golfing in MATL');
insert into paragraph (id, content) values (126, 'To see what strategy MySQL is using for your particular query. Then you can read about database theory to understand the details under the hood.');
insert into paragraph (id, content) values (127, 'Union of dense is dense?');
insert into paragraph (id, content) values (128, 'Version 1');
insert into paragraph (id, content) values (129, 'Version 2 with thanks to Kenny Lau');
insert into paragraph (id, content) values (130, 'We compute the factorial of n (manually using prod is shorter than the built-in factorial), get an array of its digits in reverse order, find the indices of the nonzero elements, get the first such index, and subtract 1.');
insert into paragraph (id, content) values (131, 'What is a word which encapsulates the meaning of both the words ''widow'' and ''female divorcee'' in one?');
insert into paragraph (id, content) values (132, 'What weapon of mass destruction could theoretically vaporize a whole solar system?');
insert into paragraph (id, content) values (133, 'What''s the best way to light up a subject that''s far away?');
insert into paragraph (id, content) values (134, 'Where can I find resources for bedtime story plots?');
insert into paragraph (id, content) values (135, 'Which is better lockup or non lockup converter?');
insert into paragraph (id, content) values (136, 'Will playing without mistakes at all become easy for me in the future?');
insert into paragraph (id, content) values (137, 'Write a radix sort');

-- Update the lexemes column
 
update paragraph set searchable_content = to_tsvector('english', 'Article ' || id || ' - ' || content);

-- Create the full text search index

create index ix1_paragraph on paragraph using gin (searchable_content);

-- Simple search, returns id and rank

select id, ts_rank_cd('using', searchable_content) from paragraph
  where searchable_content @@ to_tsquery('using');

-- Search with ranking & head lines
-- headline is expensive to compute. I.e. limit the rows.

select id, ts_rank_cd(searchable_content, query) as rank, 
    ts_headline('english', content, to_tsquery('mistake | future | reverse |without| line | loop | people | think | can'))
  from paragraph, to_tsquery('mistake | future | reverse |without| line | loop | people | think | can') query
  where query @@ searchable_content
  order by rank desc;


  

  





