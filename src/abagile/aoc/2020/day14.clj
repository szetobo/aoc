(ns abagile.aoc.2020.day14
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def sample ["mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X"
             "mem[8] = 11"
             "mem[7] = 101"
             "mem[8] = 0"])

(def sample2 ["mask = 000000000000000000000000000000X1001X"
              "mem[42] = 100"
              "mask = 00000000000000000000000000000000X0XX"
              "mem[26] = 1"])

(def input (->> (util/read-input-split-lines "2020/day14.txt")))

(defn parse [s] (->> (re-find #"mem\[(\d+)\] = (\d+)|mask = ([X01]+)" s)
                     rest
                     (#(let [[mem v mask] %]
                         (if mask
                           {:mask mask}
                           {:mem (util/parse-int mem) :value (util/parse-int v)})))))
(defn apply-mask [v mask]
  (let [vs (-> v Long/toBinaryString seq)
        cnt (count vs)
        vs (concat (repeat (- 36 cnt) \0) vs)]
    (map #(if (= %2 \X) %1 %2) vs (seq mask))))

(defn mem-sum [input]
  (->> (map parse input)
       (reduce (fn [ctx item]
                 (if (:mask item)
                   (assoc ctx :mask (:mask item))
                   (assoc-in ctx [:mem (:mem item)] (apply-mask (:value item) (:mask ctx))))))
       :mem
       (reduce-kv #(assoc %1 %2 (util/binary-val %3)) {})
       vals
       (reduce +)))

(comment
  (mem-sum sample))

(defn apply-mask2 [mem mask]
  (let [ms (-> mem Long/toBinaryString seq)
        cnt (count ms)
        ms (concat (repeat (- 36 cnt) \0) ms)]
    (->> (map #(if (= %2 \0) %1 %2) ms (seq mask))
         (reduce (fn [res item]
                   (if (= item \X)
                     (concat (map #(conj % \0) res)
                             (map #(conj % \1) res))
                     (map #(conj % item) res)))
                 [[]]))))

(comment
  (map util/binary-val (apply-mask2 42 "000000000000000000000000000000X1001X")))

(defn mem-sum2 [input]
  (->> (map parse input)
       (reduce (fn [ctx item]
                 (if (:mask item)
                   (assoc ctx :mask (:mask item))
                   (let [mem (map util/binary-val (apply-mask2 (:mem item) (:mask ctx)))]
                     (reduce (fn [ctx mem] (assoc-in ctx [:mem mem] (:value item))) ctx mem)))))
       :mem
       vals
       (reduce +)))

(comment
  (mem-sum2 sample2))

(defn part1 []
  (time (mem-sum input)))

(defn part2 []
  (time (mem-sum2 input)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
