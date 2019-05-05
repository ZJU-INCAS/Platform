package team.educoin.transaction.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.educoin.transaction.pojo.FileInfo;

import java.util.List;

@Mapper
@Component
public interface FileMapper {
    //注册资源
    @Insert("insert into file_info (id, fileOwner,fileInitialProvider,fileTitle,fileImage,fileDescription," +
            "fileReadPrice,fileOwnerPrice,fileName,fileKeyWord,fileContentType,fileFormat,fileSize) values " +
            "(#{id},#{fileOwner},#{fileInitialProvider},#{fileTitle},#{fileImage},#{fileDescription},#{fileReadPrice}," +
            "#{fileOwnerPrice},#{fileName},#{fileKeyWord},#{fileContentType},#{fileFormat},#{fileSize})")
    int registService(FileInfo fileInfo);

    //根据id获取某个资源信息
    @Select("select * from file_info where id = #{id}")
    List<FileInfo> queryFileById(@Param("id") String id);

    //获取所有未被审查的资源
    @Select("select * from file_info where fileChecked = 0")
    List<FileInfo> queryAllUnCheckedFile();

    //获取所有已经审查的资源
    @Select("select * from file_info where fileChecked = 1")
    List<FileInfo> queryAllCheckedFile();

    //查询资源列表
    @Select("select * from file_info")
    List<FileInfo> queryAllFile();

    //审核通过资源信息
    @Update("update file_info set fileChecked = 1 where id = #{id}")
    int checkFileInfo(@Param("id") String id);

    //根据id删除某个资源
    @Delete("delete from file_info where id = #{id}")
    int deleteFile(@Param("id") String id);

    //修改资源信息
    @Update("update file_info set fileTitle=#{fileTitle},fileImage=#{fileImage},fileDescription=#{fileDescription}," +
            "fileReadPrice=#{fileReadPrice},fileOwnerPrice=#{fileOwnerPrice},fileKeyWord=#{fileKeyWord}," +
            "fileContentType=#{fileContentType} where id=#{id}")
    int updateFileInfo(FileInfo fileInfo);
}
