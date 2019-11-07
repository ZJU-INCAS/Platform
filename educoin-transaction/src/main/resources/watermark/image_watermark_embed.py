from PIL import Image
import time
import numpy as np
import sys


def embedding_info(picname, text, savename):
    text += '#%#'  # As end flag
    try:
        im = np.array(Image.open(picname))
    except:
        print("Cannot obtain image, please check file name")
        time.sleep(3)
        sys.exit()

    rows, columns, colors = im.shape
    embed = []
    for c in text:
        bin_sign = (bin(ord(c))[2:]).zfill(16)
        for i in range(16):
            embed.append(int(bin_sign[i]))

    count = 0
    for row in range(rows):
        for col in range(columns):
            for color in range(colors):
                if count < len(embed):
                    im[row][col][color] = im[row][col][color] // 2 * 2 + embed[count]
                    count += 1

    Image.fromarray(im).save(savename)


if __name__ == '__main__':
    filePath = sys.argv[1]
    watermarkInfo = sys.argv[2]
    outPath = sys.argv[3]

    # filePath = "./fogbound.jpg"
    # watermarkInfo = "1234567890@gmail.com-1234567890@gmail.com-49c567e3-8ded-4422-8524-84e9a1d86d35"
    # outPath = "./out.png"

    # 将嵌入水印的图片保存为 png格式; 当前，只能提取png格式图片中的信息
    embedding_info(filePath, watermarkInfo, outPath)
    print("The digital watermark is inserted successfully!")