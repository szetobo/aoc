(ns abagile.aoc.2022.day07
  (:require
   [clojure.string :as cs]))

(def input
  (->> "resources/2022/day07.txt" slurp cs/split-lines
       (map #(mapcat rest (re-seq #"(\$ cd|\$ ls|dir|\d+)(?: (.+))?$" %)))))

(defn cd
  [state path]
  (update state :pwd #(case path "/" ["/"] ".." (pop %1) (conj %1 path))))

(defn add-dir
  [{:keys [pwd] :as state} path]
  (update-in state (concat [:ls] pwd) assoc path {}))

(defn add-file
  [{:keys [pwd] :as state} size path]
  (update-in state (concat [:ls] pwd) assoc path (read-string size)))

(defn process
  [state [ops path]]
  (case ops
    "$ cd" (cd state path)
    "$ ls" state
    "dir"  (add-dir state path)
    (add-file state ops path)))

(defn calc-dir-size
  [{:keys [pwd] :as state} path entries]
  (update state :ls (fn [ls]
                      (let [file-entries (->> entries vals (filter number?))
                            dir-entries  (reduce-kv #(cond-> %1 (map? %3) (assoc %2 %3)) {} entries)
                            pwd          (str pwd path)
                            {ls' :ls}    (reduce-kv calc-dir-size {:pwd pwd :ls ls} dir-entries)]
                        (assoc ls' pwd
                           (+ (reduce + 0 file-entries)
                              (->> (map #(get ls' (str pwd %) 0) (keys dir-entries)) (reduce + 0))))))))

(defn calc-all-dir-size
  [input]
  (->> (reduce process {:pwd ["/"] :ls {}} input)
       :ls
       (reduce-kv calc-dir-size {:pwd "" :ls {}})
       :ls))

(defn part1
  []
  (->> input calc-all-dir-size vals (filter #(<= % 100000)) (reduce +)))

(defn part2
  []
  (let [ls      (->> input calc-all-dir-size)
        to-free (- 30000000 (- 70000000 (get ls "/")))]
    (->> ls vals (filter #(>= % to-free)) sort first)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
