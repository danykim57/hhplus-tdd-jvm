포인트 충전, 차감 동시성 제어 방식 분석 및 보고서

요구사항: 유저 id 별로 포인트 충전, 차감 요청이 들어오면 들어온 요청 순서에 맞게 처리가 되어야 함

예상되는 문제점들: 

 - UserPointTable의 구현이 Thread.sleep이 들어가 있어서 작업 간의 실제 실행 종료 시간이 차이가 날 수 있음.

 - 분산 환경을 고려하지 않기에 레디스를 이용한 분산락은 제외

 - JPA를 사용하지 않아서 @Version을 이용한 동시성 제어도 제외

 - @Transaction을 걸지 않고 동시성 제어

 - 순수하게 코틀린으로 유저 id 별로 병렬 동시성 제어 처리를 하도록 시도

 - 코루틴을 이용한 동시성 제어는 오버스펙이라고 판단하여 ReentrantLock과 ConcurrentHashMap을 이용하도록 함

구상된 해결 방법:

 - ReentrantLock을 이용하여서 UserPoint를 Critical Section으로서 관리
 - ConcurrentHashMap을 이용하여서 유저 id 별로 ReentrantLock을 사용
 - 포인트의 충전과 감소 시에만 락이 걸리도록 설정
   
