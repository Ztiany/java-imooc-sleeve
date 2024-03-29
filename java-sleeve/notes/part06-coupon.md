# part06-coupon

## 优惠券的过期机制

如果要保证优惠券的状态一定是正确的，那么需要一个触发机制，到点就更新状态？

1. 主动触发：轮询，比如每 1 秒钟扫描优惠券数据库，但是这种方案不可取，会带来很大的 I/O 负担。
2. 被动触发：寻找一个第三方闹钟，达到过期点就发一个消息。第三方可选 Redis/RocketMQ。

以上只是一个假设，事实上没有必要保证优惠券的状态一定是正确的【下单的时候可以校验优惠券是否过期】。

1. 没有方案能保证优惠券的状态 100% 正确。
2. 使用开始时间和结束时间就可以对优惠券是否可用进行判断。
