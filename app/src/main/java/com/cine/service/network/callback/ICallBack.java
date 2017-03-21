package com.cine.service.network.callback;

/**
 * 
 * @author SS2101141
 *
 * @param <R>
 * 
 * Interface between Application and Library
 * 
 * <pre>
 * {@code
 * 	public void call(ICallBack callBack) {
		..
		..
		..
	}
	}
 * </pre>
 */

public interface ICallBack<R> {
	
	/**
	 * 
	 * @param response
	 * <pre>
	 * {@code @Override
		public void call(ICallBack<R> callBack) {
			if( // Condition to check the availability of service )	{
					callBack.onSuccess(response);
				} else {
					callBack.onFailure(response);
				}
			}
		}
	 * </pre>
	 */
	
	void onSuccess(R response);
	
	/**
	 * 
	 * @param response
	 * <pre>
	 * {@code @Override
		public void call(ICallBack<R> callBack) {
			if( // Condition to check the availability of service ) {
					callBack.onSuccess(response);
				} else {
					callBack.onFailure(response);
				}
			}
		}
	 * </pre>
	 */
	
	void onFailure(R response);

}
