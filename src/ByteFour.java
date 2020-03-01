import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public class ByteFour {

	private byte dataA;
	private byte dataB;
	private byte dataC;
	private byte dataD;

	public ByteFour(byte dataA, byte dataB, byte dataC, byte dataD) {
		super();
		this.dataA = dataA;
		this.dataB = dataB;
		this.dataC = dataC;
		this.dataD = dataD;
	}

	public ByteFour() {
		// TODO Auto-generated constructor stub
	}

	public byte getDataA() {
		return dataA;
	}

	public byte getDataB() {
		return dataB;
	}

	public byte getDataC() {
		return dataC;
	}

	public byte getDataD() {
		return dataD;
	}
	
	

	@Override
	public String toString() {
		return "ByteFour [dataA=" + dataA + ", dataB=" + dataB + ", dataC=" + dataC + ", dataD=" + dataD + "]";
	}

	public Color getColor(int offset) {
		byte data;
		switch (offset) {
		case 0:
			data = this.dataA;
			break;
		case 1:
			data = this.dataB;
			break;
		case 2:
			data = this.dataC;
			break;
		case 3:
			data = this.dataD;
			break;
		default:
			data = -1;

		}

		switch (data) {
		case 0:
			return Color.BLACK;
		case 1:
			return Color.RED;
		case 2:
			return Color.GREEN;
		case 3:
			return Color.BLUE;
		default:
			return Color.WHITE;
		}

	}

	public static byte getImageColor(BufferedImage image) {
		
		int n = 0, r = 0, g = 0, b = 0,w = 0;
		int dr = 0, dg = 0, db = 0;
		for(int y=0;y<image.getHeight();y++) {
			for(int x=0;x<image.getWidth();x++) {
				Color color = new Color(image.getRGB(x, y));
				//System.out.println(color.toString());
				dr+=color.getRed();dg+=color.getGreen();db+=color.getBlue();
				if (color.getRed()<127&&color.getGreen()<127&&color.getBlue()<127) {
					n++;
					continue;
				}
				if (color.getRed()>=127&&color.getGreen()>=127&&color.getBlue()>=127) {
					w++;
					continue;
				}
				if (color.getRed()>=127) {
					r++;
					continue;
				}
				if (color.getGreen()>=127) {
					g++;
					continue;
				}
				if (color.getBlue()>=127) {
					b++;
					continue;
				}
			}

			



		}
		//System.out.printf("dr:%d,dg:%d,db:%d\n", dr,dg,db);
//		try {
//			ImageIO.write(image, "JPEG", new File("H:\\"+dr+dg+db+".jpg"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//System.out.printf("n:%d,r:%d,g:%d,b:%d\n", n,r,g,b);
		int max = Math.max(Math.max(Math.max(n, r), Math.max(g, b)),w);
		if(max==n) return 0;
		if(max==r) return 1;
		if(max==g) return 2;
		if(max==b) return 3;
		if(max==w) return -1;
		return -1;
		
	}

	public static ByteFour convert(byte data) {

		byte ba, bb, bc, bd;
		ba = (byte) (data & 0x03);
		bb = (byte) ((data >> 2) & 0x03);
		bc = (byte) ((data >> 4) & 0x03);
		bd = (byte) ((data >> 6) & 0x03);
		
		ByteFour result = new ByteFour(ba, bb, bc, bd);
		System.out.println(data);
		System.out.println(result.toString());
		return result;
	}

	public static byte convert(ByteFour data) {

		byte ba, bb, bc, bd;
		ba = (byte) (data.getDataA());
		bb = (byte) (data.getDataB() << 2);
		bc = (byte) (data.getDataC() << 4);
		bd = (byte) (data.getDataD() << 6);

		byte result = (byte) (ba | bb | bc | bd);
		System.out.println(data.toString());
		System.out.println(result);
		return result;
	}

	public static ByteFour[] encode(byte[] data) {

		ByteFour[] result = new ByteFour[data.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = ByteFour.convert(data[i]);
		}

		return result;
	}

	public static byte[] decode(ByteFour[] bfd) {
		byte[] result = new byte[bfd.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = ByteFour.convert(bfd[i]);
		}
		return result;

	}

	public static BufferedImage draw(byte[] data, int width, int sizeW, int sizeH) {

		final int NUMBER = width / sizeW;
		final int LINE = data.length*4 / NUMBER + 1;
		System.out.printf("Draw Image Line:%d,Number:%d\n", LINE, NUMBER);

		BufferedImage image = new BufferedImage(NUMBER * sizeW, LINE * sizeH, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		ByteFour[] bfd = ByteFour.encode(data);
		System.out.printf("Byte Four Data Length:%d\n", bfd.length);
		
		int offset = 0;
		outer: for (int y = 0; y < LINE; y++) {
			for (int x = 0; x < NUMBER; x++) {
				System.out.printf("Draw Block %d,Data:%d\n", offset/4,offset%4);
				graphics.setColor(bfd[offset/4].getColor(offset % 4));
				

				graphics.fillRect(x * sizeW, y * sizeH, sizeW, sizeH);
				if((++offset/4)==data.length) {
					break outer;
				}
			}
		}

		return image;
	}

	public static byte[] deposit(BufferedImage image, int sizeW, int sizeH) {
		final int NUMBER = image.getWidth() / sizeW;
		final int LINE = image.getHeight() / sizeH;
		LinkedList<ByteFour> bfd = new LinkedList<ByteFour>();
		System.out.printf("Deposit Image Line:%d,Number:%d\n", LINE, NUMBER);
		int offset = 0;
		int number = 0;
		ByteFour bf = new ByteFour();
		outer: for (int y = 0; y < LINE; y++) {
			inter:for (int x = 0; x < NUMBER; x++) {
				//System.out.printf("Get Subimage:%d,%d,%d,%d\n", x*sizeW,y*sizeH,sizeW,sizeH);
				BufferedImage subImage = image.getSubimage(x*sizeW, y*sizeH, sizeW, sizeH); 
				//System.out.println(subImage.toString());
				//int[] arr = new int[sizeW*sizeH]; 
				//int[] arr = image.getRGB(x * sizeW, y * sizeH, sizeW, sizeH, null, 0, image.getWidth());
				
				//System.out.println(Arrays.toString(arr));
				byte b = getImageColor(subImage);
				
				System.out.printf("Deposit Block %d,Data:%d,Color:%d\n", number,offset%4,b);
				if(b==-1) {
					//System.err.printf("Error Block:%d", number);
					continue;
				}
				switch (offset++) {
				case 0:
					bf.dataA = b;
					break;
				case 1:
					bf.dataB = b;
					break;
				case 2:
					bf.dataC = b;
					break;
				case 3:
					bf.dataD = b;
					bfd.add(bf);
					bf = new ByteFour();
					offset = 0;
					number++;
					break;
				default:
					break;
				}

			}


		}
		ByteFour[] data = new ByteFour[bfd.size()];
		bfd.toArray(data);
		return ByteFour.decode(data);
	}
}
