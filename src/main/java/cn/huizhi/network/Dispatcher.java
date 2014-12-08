////////////////////////////////////////////////////////////////////
//                            _ooOoo_                             //
//                           o8888888o                            //    
//                           88" . "88                            //    
//                           (| ^_^ |)                            //    
//                           O\  =  /O                            //
//                        ____/`---'\____                         //                        
//                      .'  \\|     |//  `.                       //
//                     /  \\|||  :  |||//  \                      //    
//                    /  _||||| -:- |||||-  \                     //
//                    |   | \\\  -  /// |   |                     //
//                    | \_|  ''\---/''  |   |                     //        
//                    \  .-\__  `-`  ___/-. /                     //        
//                  ___`. .'  /--.--\  `. . ___                   //    
//                ."" '<  `.___\_<|>_/___.'  >'"".                //
//              | | :  `- \`.;`\ _ /`;.`/ - ` : | |               //    
//              \  \ `-.   \_ __\ /__ _/   .-` /  /               //
//        ========`-.____`-.___\_____/___.-`____.-'========       //    
//                             `=---='                            //
//        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^      //         
//                       佛祖镇楼                  BUG辟易						  //
//          	佛曰:          									  //
//                  	写字楼里写字间，写字间里程序员；                  				  //
//                  	程序人员写程序，又拿程序换酒钱。						  //
//                  	酒醒只在网上坐，酒醉还来网下眠；						  //
//                  	酒醉酒醒日复日，网上网下年复年。                                                                            //
//                  	但愿老死电脑间，不愿鞠躬老板前；                                                                            //
//                  	奔驰宝马贵者趣，公交自行程序员。                                                                            //
//                  	别人笑我忒疯癫，我笑自己命太贱；                                                                            //
//                  	不见满街漂亮妹，哪个归得程序员？                                                                            //
//                                                                //
////////////////////////////////////////////////////////////////////
package cn.huizhi.network;

import io.netty.channel.Channel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

import cn.huizhi.Player;
import cn.huizhi.WorldMgr;
import cn.huizhi.command.PlayerInfoCommand;
import cn.huizhi.message.PBMessagePro.PBMessage;
import cn.huizhi.network.handler.Command;

import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

/**
 * @copyright SHENZHEN RONG WANG HUI ZHI TECHNOLOGY CORP
 * @author Lyon.liao
 * 创建时间：2014年10月20日
 * 类说明：
 * 
 * 最后修改时间：2014年10月20日
 * 修改内容： 新建此类
 *************************************************************
 *                                    .. .vr       
 *                                qBMBBBMBMY     
 *                              8BBBBBOBMBMv    
 *                            iMBMM5vOY:BMBBv        
 *            .r,             OBM;   .: rBBBBBY     
 *            vUL             7BB   .;7. LBMMBBM.   
 *           .@Wwz.           :uvir .i:.iLMOMOBM..  
 *            vv::r;             iY. ...rv,@arqiao. 
 *             Li. i:             v:.::::7vOBBMBL.. 
 *             ,i7: vSUi,         :M7.:.,:u08OP. .  
 *               .N2k5u1ju7,..     BMGiiL7   ,i,i.  
 *                :rLjFYjvjLY7r::.  ;v  vr... rE8q;.:,, 
 *               751jSLXPFu5uU@guohezou.,1vjY2E8@Yizero.    
 *               BB:FMu rkM8Eq0PFjF15FZ0Xu15F25uuLuu25Gi.   
 *             ivSvvXL    :v58ZOGZXF2UUkFSFkU1u125uUJUUZ,   
 *           :@kevensun.      ,iY20GOXSUXkSuS2F5XXkUX5SEv.  
 *       .:i0BMBMBBOOBMUi;,        ,;8PkFP5NkPXkFqPEqqkZu.  
 *     .rqMqBBMOMMBMBBBM .           @Mars.KDIDS11kFSU5q5   
 *   .7BBOi1L1MM8BBBOMBB..,          8kqS52XkkU1Uqkk1kUEJ   
 *   .;MBZ;iiMBMBMMOBBBu ,           1OkS1F1X5kPP112F51kU   
 *     .rPY  OMBMBBBMBB2 ,.          rME5SSSFk1XPqFNkSUPZ,.
 *            ;;JuBML::r:.:.,,        SZPX0SXSP5kXGNP15UBr.
 *                L,    :@huhao.      :MNZqNXqSqXk2E0PSXPE .
 *            viLBX.,,v8Bj. i:r7:,     2Zkqq0XXSNN0NOXXSXOU 
 *          :r2. rMBGBMGi .7Y, 1i::i   vO0PMNNSXXEqP@Secbone.
 *          .i1r. .jkY,    vE. iY....  20Fq0q5X5F1S2F22uuv1M;
 *
 ***************************************************************
 */
public class Dispatcher {

	private final static ConcurrentMap<Integer, Command> COMMAND_MAP = Maps.newConcurrentMap();
	
	static {
		COMMAND_MAP.put(0x23, new PlayerInfoCommand());
	}
	
	public static class EventContext implements Event {

		private PBMessage request;
		
		private Player player;
		
		private Channel channel;
		
		public EventContext(PBMessage request, Player player, Channel channel) {
			this.request = request;
			this.player = player;
			this.channel = channel;
		}

		@Override
		public Player attachment() {
			return player;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T parseObject(Class<? extends  Message> clazz) {
			Method parseMethod;  
	        try {  
	            parseMethod = clazz.getDeclaredMethod("parseFrom", ByteString.class);
	            return (T) parseMethod.invoke(clazz, request.getData());  
	        } catch (SecurityException e) {  
	            e.printStackTrace();  
	        } catch (NoSuchMethodException e) {  
	            e.printStackTrace();  
	        } catch (IllegalArgumentException e) {  
	            e.printStackTrace();  
	        } catch (IllegalAccessException e) {  
	            e.printStackTrace();  
	        } catch (InvocationTargetException e) {  
	            e.printStackTrace();
	        }
	        return null;
		}

		@Override
		public void write(Message message) {
			PBMessage.Builder builder = PBMessage.newBuilder();
			builder.setCmd(this.request.getCmd());
			builder.setSessionId(this.player.id);
			builder.setStatus(0);
			builder.setData(message.toByteString());
			channel.writeAndFlush(builder);
		}
		
	}
	public static void dispatch(PBMessage pbMessage, Channel channel) {
		Command command = COMMAND_MAP.get(pbMessage.getCmd());
		if(command != null) {
			try {
				command.execute(new EventContext(pbMessage, WorldMgr.getPlayer(pbMessage.getSessionId()), channel));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}