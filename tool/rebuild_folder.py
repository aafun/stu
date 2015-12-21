import os, shutil

def walk_dir(dir,topdown=True):
    fileList = dict()
    for root, dirs, files in os.walk(dir):
        #print("root:", len(root), "; dirs:", len(dirs), "; files", len(files))
        #for name in root:
            #print(os.path.join(name))
            #print(os.path.join(root))
        #for name in dirs:
            #print(os.path.join(name))
            #print("  " + os.path.join(root,name))
        for name in files:
            #print(os.path.join(name))
            #print(os.path.join(root,name))
            fileList.append(os.path.join(root,name))
    return fileList
 

def copy_files_to_dir(dirSrc, dirDst):
    files = walk_dir(dirSrc)
    i = 0;
    for f in files:
        i += 1
        ext = os.path.splitext(f)[1];
        print(os.path.join(dirDst, str(i) + ext))
        shutil.copy2(f, os.path.join(dirDst, str(i) + ext))

# copy_files_to_dir("../tain_data/zimushuzi.bk", "../tain_data/zimushuzi")


# for i in os.walk("..\\train_data\\zimushuzi.bk"):
#     folder = i[0].split(os.sep)[-1]
#     fname = i[2]
#     for f in fname:
#         if not os.path.exists("..\\train_data\\zimushuzi\\" + folder):
#             os.mkdir("..\\train_data\\zimushuzi\\" + folder)
#         shutil.copy2("..\\source_data\\zimushuzi\\" + f , "..\\train_data\\zimushuzi\\" + folder + os.sep + f)


for i in os.walk("..\\train_data\\wenzi"):
    folder = i[0].split(os.sep)[-1]
    os.mkdir("..\\train_data\\tuxiang\\" + folder + ".img")
    # fname = i[2]
    # for f in fname:
    #     if not os.path.exists("..\\train_data\\zimushuzi\\" + folder):
    #         os.mkdir("..\\train_data\\zimushuzi\\" + folder)
    #     shutil.copy2("..\\source_data\\zimushuzi\\" + f , "..\\train_data\\zimushuzi\\" + folder + os.sep + f)