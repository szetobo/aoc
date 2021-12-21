(ns abagile.aoc.2021.day18
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def sample (util/read-input-split "2021/day18.sample.txt" #"\n"))
(def input  (util/read-input-split "2021/day18.txt" #"\n"))

(defn parse
  [s]
  (->> s (map read-string)))

(defn carry [vn idx n]
  (if (vector? vn) (update vn idx carry idx n) (+ vn n)))

(defn explode*
  ([s-num] (explode* 0 s-num))
  ([lvl s-num]
   (when (vector? s-num)
     (if (= lvl 4)
       [(s-num 0) 0 (s-num 1)]
       (if-let [[a+ s-num' b+] (explode* (inc lvl) (s-num 0))]
         [a+ [s-num' (carry (s-num 1) 0 b+)] 0]
         (when-let [[a+ s-num' b+] (explode* (inc lvl) (s-num 1))]
           [0 [(carry (s-num 0) 1 a+) s-num'] b+]))))))

(def explode #(or (second (explode* %)) %))

(defn split*
  [s-num]
  (if (vector? s-num)
    (if-let [s-num' (split* (s-num 0))]
      [s-num' (s-num 1)]
      (when-let [s-num' (split* (s-num 1))]
        [(s-num 0) s-num']))
    (when (<= 10 s-num)
      [(quot s-num 2) (- s-num (quot s-num 2))])))

(def split #(or (split* %) %))

;; (defn explode
;;   ([s-num]
;;    (explode 1 s-num))
;;   ([lvl [a b :as s-num]]
;;    (if (= lvl 4)
;;      (if (some vector? s-num)
;;        (if (vector? a) [0 (+ (second a) b)] [(+ a (first b)) 0])
;;        s-num)
;;      (cond
;;        (every? vector? s-num) [(explode (inc lvl) a) (explode (inc lvl) b)]
;;        ;; (let [a' (explode (inc lvl) a)]
;;        ;;   (if (not= a' a) [a' b] [a' (explode (inc lvl) b)]))
;;        (vector? a) [(explode (inc lvl) a) b]
;;        (vector? b) [a (explode (inc lvl) b)]
;;        :else s-num))))

;; (defn explode
;;   [{:keys [lvl s-num a+ b+] :or {lvl 1 a+ 0 b+ 0}}]
;;   (let [[a b] s-num]
;;     (if (= lvl 4)
;;       (if (every? number? s-num)
;;         {:lvl lvl :s-num [(+ a b+) (+ b a+)] :a+ 0 :b+ 0}
;;         (if (vector? a)
;;           {:a+ (first a)  :s-num [0 (+ (second a) b)]}
;;           {:b+ (second b) :s-num [(+ a (first b)) 0]}))
;;       (cond
;;         (every? vector? s-num)
;;         (let [{:keys [s-num] :as a'} (explode {:lvl (inc lvl) :s-num a :a+ a+ :b+ b+})]
;;           (if (not= s-num a)) [s-num b] (explode (inc lvl) b))
;;         (vector? a) [(explode (inc lvl) a) b]
;;         (vector? b) [a (explode (inc lvl) b)]
;;         :else s-num))))

(defn reduction
  [s-num]
  (loop [s-num s-num]
    (let [s-num' (second (explode* s-num))]
      (if (not= s-num' s-num)
        (recur s-num')
        (let [s-num' (split s-num)]
          (if (not= s-num' s-num)
            (recur s-num')
            s-num))))))

(defn magnitude
  [s-num]
  (if (vector? s-num)
    (+ (* 3 (magnitude (s-num 0))) (* 2 (magnitude (s-num 1))))
    s-num))

(comment
  (count sample)
  (->> (parse sample) (reduce #(reduction [%1 %2])) magnitude)
  (let [s-num (parse sample)]
    (->> (for [n1 s-num n2 s-num
               :when (not= n1 n2)]
          [n1 n2])
         (map #(magnitude (reduction %)))
         (apply max)))
  (count input)
  (parse input))

(defn part1
  []
  (time
    (->> input parse (reduce #(reduction [%1 %2])) magnitude)))

(defn part2
  []
  (time
    (let [s-num (parse input)]
      (->> (for [n1 s-num n2 s-num
                 :when (not= n1 n2)]
            [n1 n2])
           (map #(magnitude (reduction %)))
           (apply max)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= [[[[[11 8] 1] 2] 3] 4] (carry [[[[[9 8] 1] 2] 3] 4] 0 2)))
  (is (= [1 [[[[[9 8] 1] 2] 3] 6]] (carry [1  [[[[[9 8] 1] 2] 3] 4]] 1 2)))
  (is (= [[[[0 9] 2] 3] 4] (explode [[[[[9 8] 1] 2] 3] 4])))
  (is (= [7 [6 [5 [7 0]]]] (explode [7 [6 [5 [4 [3 2]]]]])))
  (is (= [[6 [5 [7 0]]] 3] (explode [[6 [5 [4 [3 2]]]] 1])))
  (is (= [[3 [2 [8 0]]] [9 [5 [4 [3 2]]]]] (explode [[3 [2 [1 [7 3]]]] [6 [5 [4 [3 2]]]]])))
  (is (= [[3 [2 [8 0]]] [9 [5 [7 0]]]]     (explode [[3 [2 [8 0]]] [9 [5 [4 [3 2]]]]])))
  (is (= [[[[0 7] 4] [[7 8] [0 13]]] [1 1]]    (split [[[[0 7] 4] [15 [0 13]]] [1 1]])))
  (is (= [[[[0 7] 4] [[7 8] [0 [6 7]]]] [1 1]] (split [[[[0 7] 4] [[7 8] [0 13]]] [1 1]])))
  (is (= [[1 2] [[3 4] 5]] (reduction [[1 2] [[3 4] 5]])))
  (is (= [[[[1 1] [2 2]] [3 3]] [4 4]] (reduce #(reduction [%1 %2]) [[1 1] [2 2] [3 3] [4 4]])))
  (is (= [[[[3 0] [5 3]] [4 4]] [5 5]] (reduce #(reduction [%1 %2]) [[1 1] [2 2] [3 3] [4 4] [5 5]])))
  (is (= [[[[5 0] [7 4]] [5 5]] [6 6]] (reduce #(reduction [%1 %2]) [[1 1] [2 2] [3 3] [4 4] [5 5] [6 6]])))
  (is (= 29  (magnitude [9 1])))
  (is (= 129 (magnitude [[9 1] [1 9]]))))
