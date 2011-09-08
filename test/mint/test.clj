(ns mint.test
  (:use [mint.core])
  (:use [clojure.test]))

(deftest wf_denoms_test
  (is (wf_denoms? {"denominations" [1,2,3,4,5]}))
  (is (false? (wf_denoms? {"denominations" [1,2,3,4]})))
  (is (false? (wf_denoms? {"denominations" [1,2,3,4,5,6]})))
  (is (false? (wf_denoms? {"denominations" [1,2,3,4,"hey"]})))
  (is (false? (wf_denoms? {"denominations" "what"}))))

(deftest wf_exchanges_test
  (is (wf_exchanges? {"exchanges" (vec (repeat 99 [1,[1,2,3,4,5]]))}))
  (is (false? (wf_exchanges? {"exchanges" (vec (repeat 98 [1,[1,2,3,4,5]]))}))))

(deftest wf_exchange_test
  (is (wf_exchange? [1,[1,0,0,0,0]]))
  (is (wf_exchange? [1,[0,-1,0,0,0]]))
  (is (false? (wf_exchange? [1,["hey",-1,0,0,0]])))
  (is (false? (wf_exchange? [1,[0]])))
  (is (false? (wf_exchange? ["hey",[0,-1,0,0,0]])))
  (is (false? (wf_exchange? [0,0,-1,0,0,0]))))

(deftest valid_exchange_test
  (is (valid_exchange? [1,2,3,4,5] 12 [3,[0,1,0,0,2]]))
  (is (valid_exchange? [1,2,3,4,5] 15 [3,[0,0,0,0,3]]))
  (is (valid_exchange? [1,2,3,4,5] 85 [3,[0,0,0,0,-3]]))
  (is (valid_exchange? [1,2,3,4,5] 87 [5,[-3,0,0,0,-2]]))
  (is (valid_exchange? [1,2,3,4,5] 92 [4,[2,0,0,0,-2]]))
  (is (valid_exchange? [1,2,3,4,5] 1 [1,[1,0,0,0,0]]))
  (is (valid_exchange? [1,2,3,4,5] 99 [1,[-1,0,0,0,0]]))
  (is (false? (valid_exchange? [1,2,3,4,5] 2 [1,[1,0,0,0,0]])))
  (is (false? (valid_exchange? [1,2,3,4,5] 20 [1,[0,0,0,0,4]])))
  (is (false? (valid_exchange? [1,2,3,4,5] 98 [2,[2,0,0,0,0]])))
  (is (false? (valid_exchange? [1,2,3,4,5] 87 [-2,[0,1,0,0,-3]])))
  (is (false? (valid_exchange? [1,2,3,4,5] 97 [1,[2,0,0,0,-1]]))))

