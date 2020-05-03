package edu.iastate.cs228.hw3;

public class Test {
	public static void main(String[] args) {
		PrimeFactorization a = new PrimeFactorization(25480);
		PrimeFactorization b = new PrimeFactorization(405);
		System.out.println(a);
		System.out.println(b);
		a.multiply(b);
		System.out.println(a);
		System.out.println(a.value());

		
//		PrimeFactorization a = new PrimeFactorization(25480);
//		PrimeFactor[] of = new PrimeFactor[1];
//		of[0] = new PrimeFactor(2, 64);
//		a = new PrimeFactorization(of);
//		a.multiply(10000);
		
		// Copy constructor test
//		PrimeFactorization pf = new PrimeFactorization(4265397L);
//		PrimeFactorization copy = new PrimeFactorization(pf);
//		System.out.println(pf);
//		System.out.println(copy);
//		copy.multiply(2);
//		System.out.println(pf);
//		System.out.println(copy);
//		System.out.println(pf.value());
//		System.out.println(copy.value());

		// Test multiply(long n)
//		PrimeFactorization pf = new PrimeFactorization(4265397L);
//		System.out.println(pf);
//		System.out.println(pf.value());
//		pf.multiply(8372728);
//		System.out.println(pf);
//		System.out.println(pf.value());
		
		// Test multiply(PrimeFactorization pf)
//		PrimeFactorization pf1 = new PrimeFactorization(783289);
//		PrimeFactorization pf2 = new PrimeFactorization(985853);
//		System.out.println(pf1);
//		System.out.println(pf2);
//		pf1.multiply(pf2);
//		System.out.println(pf1);
//		System.out.println(pf1.value());
		
		// Test multiply(pf1, pf2)
//		PrimeFactorization pf1 = new PrimeFactorization(52336478);
//		System.out.println(pf1);
//		PrimeFactorization pf2 = new PrimeFactorization(32478432);
//		System.out.println(pf2);
//		System.out.println(PrimeFactorization.multiply(pf1, pf2));
		
		// Test gcd(long n)
//		PrimeFactorization pf = new PrimeFactorization(49);
//		System.out.println(pf.gcd(56));
		
		// Test gcd(PrimeFactorization pf)
//		PrimeFactorization pf1 = new PrimeFactorization(6543);
//		PrimeFactorization pf2 = new PrimeFactorization(234);
//		System.out.println(pf1.gcd(pf2));
		
		// Test gcd(PrimeFactorization pf1, PrimeFactorization pf2
//		PrimeFactorization pf1 = new PrimeFactorization(324);
//		PrimeFactorization pf2 = new PrimeFactorization(234);
//		System.out.println(PrimeFactorization.gcd(pf1, pf2));
		
		// Test dividedBy(long n)
//		PrimeFactorization pf = new PrimeFactorization(15);
//		System.out.println(pf.dividedBy(5));
		
		// Test dividedBy(PrimeFactorization pf)
//		PrimeFactorization pf1 = new PrimeFactorization(4793142789L);
//		System.out.println(pf1);
//		PrimeFactorization pf2 = new PrimeFactorization(107);
//		System.out.println(pf2);
//		System.out.println(pf1.dividedBy(pf2));
//		System.out.println(pf1);
//		System.out.println(pf1.value());
		
		// Test dividedBy(pf1, pf2)
//		PrimeFactorization pf1 = new PrimeFactorization(45678);
//		System.out.println(pf1);
//		PrimeFactorization pf2 = new PrimeFactorization(2);
//		System.out.println(pf2);
//		PrimeFactorization pf3 = PrimeFactorization.dividedBy(pf1, pf2);
//		System.out.println(pf3);
		
		// Test array constructor
//		PrimeFactor[] pfList = new PrimeFactor[3];
//		pfList[0] = new PrimeFactor(2, 3);
//		pfList[1] = new PrimeFactor(3, 1);
//		pfList[2] = new PrimeFactor(5, 2);
//		PrimeFactorization pf = new PrimeFactorization(pfList);
//		System.out.println(pf.value());
//		

		//Project 3 Test cases

				//Test PrimeFactor class
//				PrimeFactor pf = new PrimeFactor(7, 3);
//				System.out.println(pf.toString());
				
				//Test iterator and list methods
//				PrimeFactorization test = new PrimeFactorization();
//				test.add(2, 3);
//				System.out.println(test.toString());
//
//				test.add(7, 4);
//				System.out.println(test.toString());
//
//				test.add(3, 5);
//				System.out.println(test.toString());
//
//				test.add(2, 1);
//				System.out.println(test.toString());
//
//				test.add(5, 7);
//				System.out.println(test.toString());
//				test.add(2, 3);
//				test.add(17,13);
//				test.add(5, 20);
//				test.add(11, 1);
//
//				System.out.println(test.toString());
//				
//				test.remove(5, 26);
//				System.out.println(test.toString());
//
//				test.remove(2, 8);
//				System.out.println(test.toString());
//
//				test.remove(3, 5);
//				System.out.println(test.toString());
				
				//Test Prime Test
			
//				System.out.println(PrimeFactorization.isPrime(2));
//				System.out.println(PrimeFactorization.isPrime(4));
//				System.out.println(PrimeFactorization.isPrime(7));
//				System.out.println(PrimeFactorization.isPrime(97));
//				System.out.println(PrimeFactorization.isPrime(20));
		
				
				//Test Constructors
				
				 
//				PrimeFactor[] arr = new PrimeFactor[5];
//				arr[0] = new PrimeFactor(2, 3);
//				arr[1] = new PrimeFactor(3, 4);
//				arr[2] = new PrimeFactor(7, 2);
//				arr[3] = new PrimeFactor(5, 3);
//				arr[4] = new PrimeFactor(2, 1);
//				
//				PrimeFactorization dll = new PrimeFactorization(arr);
//				System.out.println(dll.toString());
//				
//				PrimeFactorization dll2 = new PrimeFactorization(dll);
//				System.out.println(dll2.toString());
//				dll2.add(11, 2);
//
//				System.out.println(dll2.toString());
//				System.out.println(dll.toString());
				
				
//				long test = 25480;
////				long test = 330;
//				PrimeFactorization dll3 = new PrimeFactorization(test);
//				System.out.println(dll3.toString());
				
				  
				
				//Test Multipliers
				 
//				PrimeFactor[] arr = new PrimeFactor[4];
//				arr[0] = new PrimeFactor(2, 1);
//				arr[1] = new PrimeFactor(3, 4);
//				arr[2] = new PrimeFactor(11, 2);
//				arr[3] = new PrimeFactor(13, 3);
//				
//				PrimeFactorization dll = new PrimeFactorization(arr);
//				System.out.println(dll.toString());
//				
//				//multiplies 2^3 * 5 * 7^2 * 13
//				//dll.multiply(25480);
//				dll.multiply(13);
//				//dll.multiply(7);
//				System.out.println(dll.toString());
				
//				
//				
//				//Test third multiplication method
//				PrimeFactorization dll1 = new PrimeFactorization(25480);
//				System.out.println(dll1.toString());
//				
//				PrimeFactorization dll2 = new PrimeFactorization(43465);
//				System.out.println(dll2.toString());
//				
//				PrimeFactorization dll3 = PrimeFactorization.multiply(dll1, dll2);
//				System.out.println(dll3.toString());
//				  
//
//				
//				//Test divide
//				 
//				PrimeFactorization dll1 = new PrimeFactorization(25480);
//				System.out.println(dll1.toString());
//				
//				int test = 40;
//				PrimeFactorization dll2 = new PrimeFactorization(test);
//				System.out.println(dll2.toString());
//				System.out.println(dll1.dividedBy(test));
//				
//				System.out.println(dll1.toString());
//				  
//
//				
//				
//				
//				 
//				PrimeFactorization p1 =new PrimeFactorization(2354);
//				System.out.println(p1.toString());
//				PrimeFactorization p2 =new PrimeFactorization(107 * 13 * 2);
//				System.out.println(p2.toString());
//				PrimeFactorization p3= PrimeFactorization.dividedBy(p1,p2);
//				System.out.println(p3.toString());
//				  
//				
//				//Test Euclidean
//				System.out.println(PrimeFactorization.Euclidean(12, 42));
//				
//				//PrimeFactorization stuff
//				 
//				PrimeFactorization p1 =new PrimeFactorization(2*2 *5*7*17*19);
//				System.out.println(p1.toString());
//				PrimeFactorization p2 =new PrimeFactorization(2*2*2*7*13*19);
//				System.out.println(p2.toString());
//				
//				System.out.println(p1.gcd(2*2*2*7*13*19).toString());
//				
//				System.out.println(p1.gcd(p2).toString());
//				System.out.println(PrimeFactorization.gcd(p1, p2).toString());
//				  
	}
}
