(ns abagile.aoc.2015.day21
  (:gen-class)
  (:require
    [clojure.math.combinatorics :as combo]
    [clojure.test :refer [deftest is]]))

(def input {:boss   {:hp 104 :damage 8 :armor 1}
            :player {:hp 100 :damage 0 :armor 0}
            :weapons {:dagger     {:cost 8   :damage 4 :armor 0}
                      :shortsword {:cost 10  :damage 5 :armor 0}
                      :warhammer  {:cost 25  :damage 6 :armor 0}
                      :longsword  {:cost 40  :damage 7 :armor 0}
                      :greataxe   {:cost 74  :damage 8 :armor 0}}
            :armor   {:leather    {:cost 13  :damage 0 :armor 1}
                      :chainmail  {:cost 31  :damage 0 :armor 2}
                      :splintmail {:cost 53  :damage 0 :armor 3}
                      :bandedmail {:cost 75  :damage 0 :armor 4}
                      :platemail  {:cost 102 :damage 0 :armor 5}}
            :rings   {:damage+1   {:cost 25  :damage 1 :armor 0}
                      :damage+2   {:cost 50  :damage 2 :armor 0}
                      :damage+3   {:cost 100 :damage 3 :armor 0}
                      :defense+1  {:cost 20  :damage 0 :armor 1}
                      :defense+2  {:cost 40  :damage 0 :armor 2}
                      :defense+3  {:cost 80  :damage 0 :armor 3}}})

(defn play [data]
  (loop [turn 0 player (:player data) boss (:boss data)]
    (cond
      (zero? (:hp boss)) [:player turn player boss]
      (zero? (:hp player)) [:boss turn player boss]
      :else (if (even? turn)
              (let [damage (max 1 (- (:damage player) (:armor boss)))]
                (recur (inc turn) player (update-in boss [:hp] #(max 0 (- % damage)))))
              (let [damage (max 1 (- (:damage boss) (:armor player)))]
                (recur (inc turn) (update-in player [:hp] #(max 0 (- % damage))) boss))))))

(comment)

(defn part1 []
  (time (let [{:keys [weapons armor rings player boss]} input
              ans (->> (combo/subsets (keys armor)) (filter #(<= (count %) 1)))
              rns (->> (combo/subsets (keys rings)) (filter #(<= (count %) 2)))]
         (first (sort-by first (for [wn (keys weapons)
                                     [an] ans
                                     [rn1 rn2] rns
                                     :let [[wm am rm1 rm2] [(weapons wn) (armor an) (rings rn1) (rings rn2)]
                                           dp   (->> [wm am rm1 rm2] (filter some?) (map #(get % :damage 0)) (reduce +))
                                           ap   (->> [wm am rm1 rm2] (filter some?) (map #(get % :armor 0)) (reduce +))
                                           cost (->> [wm am rm1 rm2] (filter some?) (map #(get % :cost 0)) (reduce +))
                                           player' (-> player
                                                       (update-in [:damage] #(+ % dp))
                                                       (update-in [:armor] #(+ % ap))
                                                       (update-in [:cost] #(+ (or % 0) cost)))
                                           res (play {:player player' :boss boss})]
                                     :when (= :player (first res))]
                                  [(:cost player') wn an rn1 rn2 player']))))))

(defn part2 []
  (time (let [{:keys [weapons armor rings player boss]} input
              ans (->> (combo/subsets (keys armor)) (filter #(<= (count %) 1)))
              rns (->> (combo/subsets (keys rings)) (filter #(<= (count %) 2)))]
         (first (sort-by first > (for [wn (keys weapons)
                                       [an] ans
                                       [rn1 rn2] rns
                                       :let [[wm am rm1 rm2] [(weapons wn) (armor an) (rings rn1) (rings rn2)]
                                             dp   (->> [wm am rm1 rm2] (filter some?) (map #(get % :damage 0)) (reduce +))
                                             ap   (->> [wm am rm1 rm2] (filter some?) (map #(get % :armor 0)) (reduce +))
                                             cost (->> [wm am rm1 rm2] (filter some?) (map #(get % :cost 0)) (reduce +))
                                             player' (-> player
                                                         (update-in [:damage] #(+ % dp))
                                                         (update-in [:armor] #(+ % ap))
                                                         (update-in [:cost] #(+ (or % 0) cost)))
                                             res (play {:player player' :boss boss})]
                                       :when (= :boss (first res))]
                                   [(:cost player') wn an rn1 rn2 player']))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-sample
  (is (= (play {:player {:hp 8  :damage 5 :armor 5}
                :boss   {:hp 12 :damage 7 :armor 2}})
         [:player 7 {:hp 2 :damage 5 :armor 5} {:hp 0 :damage 7 :armor 2}])))

(deftest test-input
  (is (= (play input)
         [:boss 26 {:hp 0 :damage 0 :armor 0} {:hp 91 :damage 8 :armor 1}])))
