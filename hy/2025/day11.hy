(import sys)
(import functools [cache])
(import hyrule *)
(require hyrule *)

(setv p1 0
      p2 0)

(setv D (.read sys.stdin)
      lines (lfor line (.split D "\n") :if line line)
      M {})

(for [line lines]
  (setv [src outs] (.split line ":"))
  (assoc M src (.split outs)))

(defn f [x]
  (if (= x "out") 1 (sum (gfor out (.get M x []) (f out)))))

(setv p1 (f "you"))

(defn [cache] g [x fft dac]
  (if (= x "out")
    (if (and fft dac) 1 0)
    (sum (gfor out (.get M x []) (g out (or fft (= out "fft")) (or dac (= out "dac")))))))

(setv p2 (g "svr" False False))

(print f"The result for part 1: {p1}")
(print f"The result for part 2: {p2}")
