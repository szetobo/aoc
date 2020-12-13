(ns abagile.aoc.2020.day13
  (:gen-class))
; (:require))
;   [clojure.string :as cs]))

(def sample ["939"
             "7,13,x,x,59,x,31,19"])

(def input ["1006697"
            "13,x,x,41,x,x,x,x,x,x,x,x,x,641,x,x,x,x,x,x,x,x,x,x,x,19,x,x,x,x,17,x,x,x,x,x,x,x,x,x,x,x,29,x,661,x,x,x,x,x,37,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,23"])

(def sample1 "17,x,13,19")
(def sample2 "67,7,59,61")
(def sample3 "67,x,7,59,61")
(def sample4 "67,7,x,59,61")
(def sample5 "1789,37,47,1889")

(defn parse [s] (->> (re-seq #"(\d+)" s)
                     (mapcat rest)
                     ; first))
                     (map read-string)))

(defn parse-x [s] (->> (re-seq #"(\d+|x)" s)
                       (mapcat rest)
                       ; first))
                       (map read-string)))

(defn bus-avail [tm bus-no]
  (let [a (* bus-no (int (/ tm (double bus-no))))]
    (if (>= a tm) a (+ a bus-no))))

(defn next-bus [input]
  (let [[tm & buses] (flatten (map parse input))]
    (->> (for [b buses] [b (- (bus-avail tm b) tm)])
         (sort-by second))))

(defn time-diff [input]
  (loop [i 0
         res []
         [h & t] input]
    (if (= i (count input))
      res
      (recur (inc i)
             (if (number? h) (conj res i) res)
             t))))

(defn bus-in-line [input]
  (let [s (parse-x input)
        b (filter number? s)
        d (time-diff s)
        i (first b)]
    (loop [t i
           res []]
      (if (= (count res) (count b))
        [t res]
        (let [a (map #(bus-avail t %) b)
              n (map #(+ % (first a)) d)]
          (when (zero? (mod t (* i 1000000))) (prn t a n))
          (if (= a n)
            (recur t a)
            (recur (+ t i) [])))))))


(comment
  (flatten (map parse sample))
  (* 7 (int (/ 939 (double 7))))
  (* 7 (int (/ 938 (double 7))))
  (bus-avail 939 7)
  (bus-avail 939 59)
  (->> (next-bus sample) first (apply *))
  (->> (second (map parse-x sample))
       time-diff)
  (parse-x sample1)
  (bus-in-line sample5)
  (bus-in-line (second input)))



(defn mod-inv [a n]
  (let [a (mod a n)]
    (first (filter #(= (mod (* a %) n) 1) (range 1 n)))))

(defn parse-y [s] (->> (re-seq #"(\d+|x)" s)
                       (mapcat rest)
                       (map-indexed #(when (not= %2 "x")
                                       (let [bus (read-string %2)]
                                         [(mod (- bus %1) bus) bus])))
                       (filter some?)))

(defn bus-align [input]
  (let [s  (parse-y input)
        fp (apply * (map second s))]
    (->> (map (fn [[r bus]]
                (let [pp (/ fp bus)
                      iv (mod-inv pp bus)]
                  (* iv pp r)))
              s)
         (reduce +)
         (#(mod % fp)))))

(comment
  (parse-y (second sample))
  (bus-align (second sample))
  (bus-align sample1)
  (bus-align sample2)
  (bus-align sample3)
  (bus-align sample4)
  (bus-align (second input)))

(defn -main [& _]
  (println "part 1:"
           (->> (next-bus input) first (apply *)))

  (println "part 2:"
           (bus-align (second input))))
