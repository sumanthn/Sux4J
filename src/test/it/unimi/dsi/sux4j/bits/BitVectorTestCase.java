package test.it.unimi.dsi.sux4j.bits;

import it.unimi.dsi.fastutil.io.BinIO;
import it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
import it.unimi.dsi.fastutil.longs.LongListIterator;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import it.unimi.dsi.sux4j.bits.BitVector;
import it.unimi.dsi.sux4j.bits.BooleanListBitVector;
import it.unimi.dsi.sux4j.bits.LongArrayBitVector;
import it.unimi.dsi.sux4j.bits.LongBigList;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

public abstract class BitVectorTestCase extends TestCase {

	public static void testSetClearFlip( final BitVector v ) {
		final int size = v.size();
		for( int i = size; i-- != 0; ) {
			v.set( i );
			assertTrue( v.getBoolean( i ) );
		}

		for( int i = size; i-- != 0; ) {
			v.clear( i );
			assertFalse( v.getBoolean( i ) );
		}

		for( int i = size; i-- != 0; ) {
			v.set( i );
			v.flip( i );
			assertFalse( v.getBoolean( i ) );
			v.flip( i );
			assertTrue( v.getBoolean( i ) );
		}
	}
	
	
	public static void testRemove( final BitVector v ) {
		v.clear();
		v.size( 100 );
		v.set( 0, true );
		assertTrue( v.removeBoolean( 0 ) );
		assertFalse( v.getBoolean( 0 ) );

		v.clear();
		v.size( 100 );
		v.set( 63, true );
		v.set( 64, true );
		assertTrue( v.removeBoolean( 63 ) );
		assertTrue( v.getBoolean( 63 ) );
		assertFalse( v.getBoolean( 64 ) );
		assertFalse( v.getBoolean( 0 ) );
	}
	
	public static void testAdd( final BitVector v ) {
		v.clear();
		v.size( 100 );
		v.add( 0, true );
		assertTrue( v.getBoolean( 0 ) );
		v.add( 0, true );
		assertTrue( v.getBoolean( 0 ) );
		assertTrue( v.getBoolean( 1 ) );
		v.add( false );
		assertTrue( v.getBoolean( 0 ) );
		assertTrue( v.getBoolean( 1 ) );
		assertFalse( v.getBoolean( 2 ) );
		v.set( 1, false );
		assertTrue( v.getBoolean( 0 ) );
		assertFalse( v.getBoolean( 1 ) );
		assertFalse( v.getBoolean( 2 ) );
		v.set( 1, true );
		assertTrue( v.getBoolean( 0 ) );
		assertTrue( v.getBoolean( 1 ) );
		assertFalse( v.getBoolean( 2 ) );
		
		v.clear();
		v.size( 100 );
		v.add( 0, 1 );
		assertEquals( 1, v.getInt( 0 ) );
		v.add( 0, 2 );
		assertEquals( 1, v.getInt( 0 ) );
		assertEquals( 1, v.getInt( 1 ) );
		v.add( 0, 0 );
		assertEquals( 0, v.getInt( 0 ) );
		assertEquals( 1, v.getInt( 1 ) );
		assertEquals( 1, v.getInt( 2 ) );
		v.add( 0 );
		assertEquals( 0, v.getInt( 0 ) );
		assertEquals( 1, v.getInt( 1 ) );
		assertEquals( 1, v.getInt( 2 ) );
		assertEquals( 0, v.getInt( 3 ) );
		v.set( 1, 0 );
		assertEquals( 0, v.getInt( 0 ) );
		assertEquals( 0, v.getInt( 1 ) );
		assertEquals( 1, v.getInt( 2 ) );
		assertEquals( 0, v.getInt( 3 ) );
		v.set( 1, 1 );
		assertEquals( 0, v.getInt( 0 ) );
		assertEquals( 1, v.getInt( 1 ) );
		assertEquals( 1, v.getInt( 2 ) );
		assertEquals( 0, v.getInt( 3 ) );
		
		v.clear();
		v.append( 1, 2 );
		v.append( 1, 2 );
		v.append( 3, 2 );
		assertEquals( LongArrayBitVector.of( 1, 0, 1, 0, 1, 1 ), v );

	
		v.clear();
		for( int i = 0; i < 80; i++ ) v.add( 0, true );
		for( int i = 0; i < 80; i++ ) assertTrue( v.getBoolean( i ) );

		v.clear();
		for( int i = 0; i < 80; i++ ) v.add( 0, false );
		for( int i = 0; i < 80; i++ ) assertFalse( v.getBoolean( i ) );
	}
	
	public static void testFillFlip( final BitVector v ) {

		v.clear();
		v.size( 100 );
		v.fill( true );
		for( int i = v.size(); i-- != 0; ) assertTrue( v.getBoolean( i ) );
		v.fill( false );
		for( int i = v.size(); i-- != 0; ) assertFalse( v.getBoolean( i ) );
		v.flip();
		for( int i = v.size(); i-- != 0; ) assertTrue( v.getBoolean( i ) );
		
		v.clear();
		v.size( 100 );
		v.fill( 1 );
		for( int i = v.size(); i-- != 0; ) assertTrue( v.getBoolean( i ) );
		v.fill( 0 );
		for( int i = v.size(); i-- != 0; ) assertFalse( v.getBoolean( i ) );
		
		v.clear();
		v.size( 100 );
		v.fill( 5, 70, true );
		for( int i = v.size(); i-- != 0; ) assertEquals( i >= 5 && i < 70, v.getBoolean( i ) );
		v.fill( true );
		v.fill( 5, 70, false );
		for( int i = v.size(); i-- != 0; ) assertEquals( i < 5 || i >= 70, v.getBoolean( i ) );
		
		v.clear();
		v.size( 100 );
		v.fill( 5, 70, 1 );
		for( int i = v.size(); i-- != 0; ) assertEquals( Integer.toString(  i  ), i >= 5 && i < 70, v.getBoolean( i ) );
		v.fill( true );
		v.fill( 5, 70, 0 );
		for( int i = v.size(); i-- != 0; ) assertEquals( i < 5 || i >= 70, v.getBoolean( i ) );
		
		v.clear();
		v.size( 100 );
		v.flip( 5, 70 );
		for( int i = v.size(); i-- != 0; ) assertEquals( Integer.toString(  i  ), i >= 5 && i < 70, v.getBoolean( i ) );
		v.fill( true );
		v.flip( 5, 70 );
		for( int i = v.size(); i-- != 0; ) assertEquals( i < 5 || i >= 70, v.getBoolean( i ) );	
	}
	
	
	public static void testCopy( final BitVector v ) {
		v.clear();
		v.size( 100 );
		v.fill( 5, 80, true );
		BitVector w = v.copy( 0, 85 );
		assertEquals( w, v.subVector( 0, 85 ).copy() );
		
		for( int i = w.size(); i-- != 0; ) assertEquals( i >= 5 && i < 80, w.getBoolean( i ) );	
		w = v.copy( 5, 85 );
		assertEquals( w, v.subVector( 5, 85 ).copy() );
		for( int i = w.size(); i-- != 0; ) assertEquals( i < 75, w.getBoolean( i ) );	

		
		v.clear();
		int[] bits = { 0,0,0,0,1,1,1,0,0,0,0,1,1,0,0 };
		for( int i = 0; i < bits.length; i++ ) v.add( bits[ i ] );
		
		LongArrayBitVector c = LongArrayBitVector.getInstance();
		for( int i = 5; i < bits.length; i++ ) c.add( bits[ i ] );
		
		assertEquals( c, v.copy( 5, 15 ) );
		assertEquals( c, v.subVector( 5, 15 ).copy() );

		v.clear();
		bits = new int[] { 0,0,0,0,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,0,0,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,0,0,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,0,0,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,0,0,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,0,0,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,0,0 };
		for( int i = 0; i < bits.length; i++ ) v.add( bits[ i ] );
		c = LongArrayBitVector.getInstance();
		for( int i = 5; i < bits.length - 2; i++ ) c.add( bits[ i ] );
		
		assertEquals( c, v.copy( 5, bits.length - 2 ) );
		
		assertEquals( v, v.copy() );
	}
	
	public static void testBits( BitVector b ) {
		for( int i = 0; i < 100; i++ ) b.add( i % 2 );
		assertTrue( LongArrayBitVector.wrap( b.bits(), b.length() ).toString(), Arrays.equals( new long[] { 0xAAAAAAAAAAAAAAAAL, 0x0000000AAAAAAAAAL }, b.bits() ) );
	}

	public static void testLongBigListView( BitVector b ) {
		LongBigList list = b.asLongBigList( 10 );
		for( int i = 0; i < 100; i++ ) list.add( i );
		for( int i = 0; i < 100; i++ ) assertEquals( i, list.getLong( i ) );
		for( int i = 0; i < 100; i++ ) list.add( i );
		for( int i = 0; i < 100; i++ ) {
			assertEquals( i, list.set( i, i + 1 ) );
			for( int j = i + 1; j < 100; j++ ) assertEquals( "" + i , j, list.getLong( j ) );
		}
		for( int i = 0; i < 100; i++ ) assertEquals( i + 1, list.getLong( i ) );
		list.size( 100 );
		int k = 0;
		LongListIterator i = list.listIterator();
		while( i.hasNext() ) {
			assertEquals( k, i.nextIndex() );
			assertEquals( ++k, i.nextLong() ); 
		}
		while( i.hasPrevious() ) {
			assertEquals( k - 1, i.previousIndex() );
			assertEquals( k--, i.previousLong() );
		}
	}

	public static void testLongSetView( BitVector b ) {
		LongSortedSet s = b.asLongSet();
		assertNull( s.comparator() );
		
		for( int i = 0; i < 100; i++ ) s.add( i * 2 );
		for( int i = 0; i < 100; i++ ) assertTrue( s.contains( i * 2 ) );
		for( int i = 0; i < 100; i++ ) assertFalse( s.contains( i * 2 + 1 ) );
		
		LongBidirectionalIterator iterator = s.iterator();
		for( int i = 0; i < 100; i++ ) assertEquals( i * 2, iterator.nextLong() );
		assertFalse( iterator.hasNext() );
		for( int i = 100; i-- != 0; ) assertEquals( i * 2, iterator.previousLong() );
		assertFalse( iterator.hasPrevious() );
		
		assertEquals( 100, s.size() );
		assertEquals( false, s.remove( 1 ) );
		assertEquals( 100, s.size() );
		assertEquals( true, s.remove( 0 ) );
		assertEquals( 99, s.size() );
		assertEquals( true, s.remove( 60 ) );
		assertEquals( 98, s.size() );
		assertEquals( false, s.remove( 1000 ) );
		assertEquals( 98, s.size() );
		assertEquals( false, s.add( 2 ) );
		assertEquals( 98, s.size() );
		
		assertEquals( 2, s.firstLong() );
		assertEquals( 198, s.lastLong() );
		
		assertEquals( 18, s.subSet( 3, 40 ).size());
		assertEquals( 19, s.headSet( 40 ).size());
		assertEquals( 5, s.tailSet( 190 ).size());
		
		iterator = s.iterator();
		assertEquals( 2, iterator.nextLong() );
		iterator.remove();
		assertFalse( s.contains( 2 ) );
		
	}

	public static void testFirstLastPrefix( BitVector b ) {
		b.clear();
		b.length( 60 );
		assertEquals( -1, b.firstOne() );
		assertEquals( -1, b.lastOne() );

		b.set( 4, true );
		assertEquals( 4, b.firstOne() );
		assertEquals( 4, b.lastOne() );
		b.set( 50, true );
		assertEquals( 4, b.firstOne() );
		assertEquals( 50, b.lastOne() );
		b.set( 20, true );
		assertEquals( 4, b.firstOne() );
		assertEquals( 50, b.lastOne() );
		b.length( 100 );
		b.set( 90, true );
		assertEquals( 4, b.firstOne() );
		assertEquals( 90, b.lastOne() );
		b.clear();
		b.length( 100 );
		assertEquals( -1, b.firstOne() );
		assertEquals( -1, b.lastOne() );

		b.set( 4, true );
		assertEquals( 4, b.firstOne() );
		assertEquals( 4, b.lastOne() );
		b.set( 90, true );
		assertEquals( 4, b.firstOne() );
		assertEquals( 90, b.lastOne() );
		
		b.length( 60 );
		BitVector c = b.copy();
		c.length( 40 );
		assertEquals( c.length(), b.maximumCommonPrefixLength( c ) );
		c.flip( 20 );
		assertEquals( 20, b.maximumCommonPrefixLength( c ) );
		c.flip( 0 );
		assertEquals( 0, b.maximumCommonPrefixLength( c ) );

		b.length( 100 );
		c = b.copy();
		c.length( 80 );
		assertEquals( c.length(), b.maximumCommonPrefixLength( c ) );
		assertEquals( c.length(), b.maximumCommonPrefixLength( BooleanListBitVector.wrap( c ) ) );
		c.flip( 20 );
		assertEquals( 20, b.maximumCommonPrefixLength( c ) );
		assertEquals( 20, b.maximumCommonPrefixLength( BooleanListBitVector.wrap( c ) ) );

		c.flip( 0 );
		assertEquals( 0, b.maximumCommonPrefixLength( c ) );
		assertEquals( 0, b.maximumCommonPrefixLength( BooleanListBitVector.wrap( c ) ) );
		
		c.clear();
		c.length( 2 );
		c.set( 0, 0 );
		assertFalse( c.getBoolean( 0 ) );
		c.set( 0, 1 );
		assertTrue( c.getBoolean( 0 ) );
		c.set( 0, 2 );
		assertTrue( c.getBoolean( 0 ) );
		c.set( 0, 0 );
		assertFalse( c.getBoolean( 0 ) );
		c.add( 0, 0 );
		assertFalse( c.getBoolean( 0 ) );
		c.add( 0, 1 );
		assertTrue( c.getBoolean( 0 ) );
	}

	public static void testLogicOperators( BitVector b ) {
		b.clear();
		b.length( 100 );
		BitVector c = b.copy();
		for( int i = 0; i < 50; i++ ) b.set( 2 * i );
		for( int i = 0; i < 50; i++ ) c.set( 2 * i + 1 );
		BitVector r;

		r = b.copy().and( c );
		for( int i = 0; i < 100; i++ ) assertFalse( r.getBoolean( i ) );
		r = b.copy().or( c );
		for( int i = 0; i < 100; i++ ) assertTrue( r.getBoolean( i ) );
		r = b.copy().xor( c );
		for( int i = 0; i < 100; i++ ) assertTrue( r.getBoolean( i ) );
		r.xor( r );
		for( int i = 0; i < 100; i++ ) assertFalse( r.getBoolean( i ) );

		r = b.copy().and( BooleanListBitVector.wrap( c ) );
		for( int i = 0; i < 100; i++ ) assertFalse( r.getBoolean( i ) );
		r = b.copy().or( BooleanListBitVector.wrap( c ) );
		for( int i = 0; i < 100; i++ ) assertTrue( r.getBoolean( i ) );
		r = b.copy().xor( BooleanListBitVector.wrap( c ) );
		for( int i = 0; i < 100; i++ ) assertTrue( r.getBoolean( i ) );
		r.xor( BooleanListBitVector.wrap( r ) );
		for( int i = 0; i < 100; i++ ) assertFalse( r.getBoolean( i ) );
	}
	
	public static void testCount( BitVector b ) {
		b.clear();
		b.length( 100 );
		for( int i = 0; i < 50; i++ ) b.set( i * 2 );
		assertEquals( 50, b.count() );
	}

	public static void testSerialisation( BitVector b ) throws IOException, ClassNotFoundException {
		final File file = File.createTempFile( BitVectorTestCase.class.getSimpleName(), "test" );
		file.deleteOnExit();

		b.clear();
		
		BinIO.storeObject( b, file );
		assertEquals( b, BinIO.loadObject( file ) );

		b.length( 1000 );
		
		BinIO.storeObject( b, file );
		assertEquals( b, BinIO.loadObject( file ) );

		b.fill( true );
		
		BinIO.storeObject( b, file );
		assertEquals( b, BinIO.loadObject( file ) );
	}
}