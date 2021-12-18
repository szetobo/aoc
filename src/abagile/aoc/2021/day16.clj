(ns abagile.aoc.2021.day16
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.pprint :as pp]
    [clojure.string :as cs]
    [clojure.test :refer [deftest is]]))

(def sample ["8A004A801A8002F478"
             "620080001611562C8802118E34"
             "C0015000016115A2E0802F182340"
             "A0016C880162017C3686B18A3D4780"])

(def input  (util/read-input "2021/day16.txt"))

(defn parse
  [s]
  (->> (cs/split (cs/replace s #"\n" "") #"") (map #(Integer/parseInt % 16)) (map #(pp/cl-format nil "~4,'0b" %)) (apply str)))

(def exp-type #(if (= (subs % 3 6) "100") :literal :operator))

(defmulti decode #'exp-type)

(defmethod decode :literal
  [s]
  (let [[bits vs] (loop [v (subs s 6) bits 6 vs ""]
                    (if (= \0 (first v))
                      [(+ bits 5) (str vs (subs v 1 5))]
                      (recur (subs v 5) (+ bits 5) (str vs (subs v 1 5)))))]
    (assoc (util/fmap util/binary-val {:type (subs s 3 6) :ver (subs s 0 3) :val vs}) :bits bits)))

(defmethod decode :operator
  [s]
  (if (= \0 (first (subs s 6 7)))
    (let [len (util/binary-val (subs s 7 22))
          exprs (loop [exprs [] ss (subs s 22) len len]
                  (if (zero? len)
                    exprs
                    (let [{:keys [bits] :as expr} (decode ss)]
                      (recur (conj exprs expr) (subs ss bits) (- len bits)))))]
      (assoc (util/fmap util/binary-val {:type (subs s 3 6) :ver (subs s 0 3)}) :bits (+ 22 len) :sub exprs))
    (let [cnt  (util/binary-val (subs s 7 18))
          exprs (loop [exprs [] ss (subs s 18) cnt cnt]
                  (if (zero? cnt)
                    exprs
                    (let [{:keys [bits] :as expr} (decode ss)]
                      (recur (conj exprs expr) (subs ss bits) (dec cnt)))))
          bits    (+ 18 (reduce + (map :bits exprs)))]
      (assoc (util/fmap util/binary-val {:type (subs s 3 6) :ver (subs s 0 3)}) :bits bits :sub exprs))))

(defn versions
  [{:keys [ver sub]}]
  (if sub
    (concat [ver] (mapcat versions sub))
    [ver]))

(defn eval-expr
  [{:keys [type sub val]}]
  (case type
    0 (reduce + (map eval-expr sub))
    1 (reduce * (map eval-expr sub))
    2 (apply min (map eval-expr sub))
    3 (apply max (map eval-expr sub))
    4 val
    5 (let [[a b] sub] (if (> (eval-expr a) (eval-expr b)) 1 0))
    6 (let [[a b] sub] (if (< (eval-expr a) (eval-expr b)) 1 0))
    7 (let [[a b] sub] (if (= (eval-expr a) (eval-expr b)) 1 0))))

(defn transpile
  [{:keys [type sub val]}]
  (case type
    0 `(+ ~@(map transpile sub))
    1 `(* ~@(map transpile sub))
    2 `(min ~@(map transpile sub))
    3 `(max ~@(map transpile sub))
    4 val
    5 `(if (> ~(transpile (first sub)) ~(transpile (second sub))) 1 0)
    6 `(if (< ~(transpile (first sub)) ~(transpile (second sub))) 1 0)
    7 `(if (= ~(transpile (first sub)) ~(transpile (second sub))) 1 0)))

(comment
  (count sample)
  (count input)
  (parse input))

(deftest example
  (is (= "100010100000000001001010100000000001101010000000000000101111010001111000"                                                 (parse (sample 0))))
  (is (= "01100010000000001000000000000000000101100001000101010110001011001000100000000010000100011000111000110100"                 (parse (sample 1))))
  (is (= "1100000000000001010100000000000000000001011000010001010110100010111000001000000000101111000110000010001101000000"         (parse (sample 2))))
  (is (= "101000000000000101101100100010000000000101100010000000010111110000110110100001101011000110001010001111010100011110000000" (parse (sample 3))))
  (is (= 16 (->> (decode (parse (sample 0))) versions (reduce +))))
  (is (= 12 (->> (decode (parse (sample 1))) versions (reduce +))))
  (is (= 23 (->> (decode (parse (sample 2))) versions (reduce +))))
  (is (= 31 (->> (decode (parse (sample 3))) versions (reduce +))))
  (is (= 3  (eval-expr (decode (parse "C200B40A82")))))
  (is (= 54 (eval-expr (decode (parse "04005AC33890")))))
  (is (= 7  (eval-expr (decode (parse "880086C3E88112")))))
  (is (= 9  (eval-expr (decode (parse "CE00C43D881120")))))
  (is (= 1  (eval-expr (decode (parse "D8005AC2A8F0")))))
  (is (= 0  (eval-expr (decode (parse "F600BC2D8F")))))
  (is (= 0  (eval-expr (decode (parse "9C005AC2F8F0")))))
  (is (= 1  (eval-expr (decode (parse "9C0141080250320F1802104A08")))))
  (is (= 3  (eval (transpile (decode (parse "C200B40A82"))))))
  (is (= 54 (eval (transpile (decode (parse "04005AC33890"))))))
  (is (= 7  (eval (transpile (decode (parse "880086C3E88112"))))))
  (is (= 9  (eval (transpile (decode (parse "CE00C43D881120"))))))
  (is (= 1  (eval (transpile (decode (parse "D8005AC2A8F0"))))))
  (is (= 0  (eval (transpile (decode (parse "F600BC2D8F"))))))
  (is (= 0  (eval (transpile (decode (parse "9C005AC2F8F0"))))))
  (is (= 1  (eval (transpile (decode (parse "9C0141080250320F1802104A08")))))))

(defn part1
  []
  (time
    (->> (decode (parse input)) versions (reduce +))))

(defn part2
  []
  (time
    (->> (decode (parse input)) eval-expr)))
    ;; (->> (decode (parse input)) transpile eval)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
