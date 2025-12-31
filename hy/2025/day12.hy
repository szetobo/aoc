(import sys)
(import re)
(import hyrule *)
(require hyrule *)

(setv p1 0
      p2 0)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line line)
      S []
      T [])

(for [line lines]
  (cond
   (= 2 (len line))
   (do (.append S [])
       (.append T 0))

   (re.match r"[#.]+" line)
   (do (.append (get S -1) (.split line))
       (+= (get T -1) (len (lfor x line :if (= x "#") 1))))

   True
   (do (setv [w h #* others] (lfor x (re.findall r"\d+" line) (int x))
             ttl_t (* w h)
             min_p (* (/ w 3) (/ h 3))
             cnt_p (sum others)
             cnt_t (sum (lfor [i v] (enumerate others) (* v (get T i)))))
       (when (and (<= cnt_p min_p) (<= cnt_t ttl_t)) (+= p1 1)))))


(print f"The result for part 1: {p1}")
(print f"The result for part 2: {p2}")
