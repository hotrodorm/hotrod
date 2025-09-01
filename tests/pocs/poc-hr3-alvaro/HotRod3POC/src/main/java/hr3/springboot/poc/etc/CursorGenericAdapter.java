package hr3.springboot.poc.etc;

import java.io.IOException;
import java.util.Iterator;

import org.hotrod.runtime.cursors.Cursor;

public class CursorGenericAdapter<I, C extends I> implements Cursor<I> {
	private Cursor<C> cursor;

	public CursorGenericAdapter(final Cursor<C> cursor) {
		this.cursor = cursor;
	}

	@Override
	public void close() throws IOException {
		this.cursor.close();
	}

	@Override
	public Iterator<I> iterator() {
		return new CursorIterator<I, C>(this.cursor.iterator());
	}

	public class CursorIterator<I, C extends I> implements Iterator<I> {

		private Iterator<C> it;

		public CursorIterator(final Iterator<C> it) {
			this.it = it;
		}

		@Override
		public boolean hasNext() {
			return this.it.hasNext();
		}

		@Override
		public I next() {
			return this.it.next();
		}

	}
}
