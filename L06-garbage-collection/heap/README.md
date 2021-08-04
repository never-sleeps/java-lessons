

|                                | -Xmx256m           | -Xmx512m           | -Xmx768m           | -Xmx1024m          | -Xmx2048m          |
|--------------------------------|--------------------|--------------------|--------------------|--------------------|--------------------|
| no optimization                | msec:23270, sec:23 | msec:12806, sec:12 | msec:12340, sec:12 | msec:12646, sec:12 | msec:13365, sec:13 |
| optimization (Integer -> int)  | msec:2737, sec:2   | msec:2547, sec:2   | msec:2226, sec:2   | msec:2388, sec:2   | msec:2409, sec:2   |
| optimization (Data -> Integer) | msec:2649, sec:2   | msec:2169, sec:2   | msec:2272, sec:2   | msec:2417, sec:2   | msec:2327, sec:2   |