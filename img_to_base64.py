import base64

def image_to_base64(image_path):
    with open(image_path, "rb") as image_file:
        encoded_string = base64.b64encode(image_file.read()).decode('utf-8')
    return encoded_string
    
# 示例
image_paths = ["img.png", "img_1.png", "img_2.png", "img_3.png"]
base64_images = {path: image_to_base64(path) for path in image_paths}
for path, base64_image in base64_images.items():
    print(f"![{path}](data:image/png;base64,{base64_image})")