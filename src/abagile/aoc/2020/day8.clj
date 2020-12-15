(ns abagile.aoc.2020.day8
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def input (->> (util/read-input "2020/day8.txt")))

(defn parse [code]
  (vec (for [[_ op v] (re-seq #"(nop|acc|jmp) ([+-]\d+)" code)]
         [(keyword op) (util/parse-int v)])))

(defn run [codes]
  (let [lines (count codes)]
    (loop [{:keys [ln visited?] :as ctx} {:var 0 :ln 0 :visited? #{}}] 
      (cond
        (visited? ln)
        (assoc ctx :halted? true)

        (>= ln lines)
        ctx

        :else
        (let [[op v] (nth codes ln)]
          (case op
            :nop (recur (-> ctx
                            (update :ln inc)
                            (update :visited? conj ln)))
            :acc (recur (-> ctx
                            (update :var + v)
                            (update :ln inc)
                            (update :visited? conj ln)))
            :jmp (recur (-> ctx
                            (update :ln + v)
                            (update :visited? conj ln)))))))))

(defn part1 []
  (time (-> (run (parse input))
           :var)))

(defn part2 []
  (time (-> (let [codes (parse input)]
             (first (for [ln (range (count codes))
                          :when (#{:nop :jmp} (get-in codes [ln 0]))
                          :let [ctx (run (update-in codes [ln 0] {:jmp :nop :nop :jmp}))]
                          :when (nil? (:halted? ctx))]
                     [ln ctx])))
           second
           :var)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
