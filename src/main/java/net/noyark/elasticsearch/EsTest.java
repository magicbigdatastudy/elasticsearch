package net.noyark.elasticsearch;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;

public class EsTest {

    public TransportClient getClient() throws Exception{
        return new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(
                        new TransportAddress(InetAddress.getByName("mapi.noyark.net"),9300)
                );
        //如果是多个节点，则在add后面执行后，继续调用一个add
    }

    /**
     * 连接
     * @throws Exception
     */

    @Test
    public void test01() throws Exception{
        //java TransportClient client 连接es
        //Empty表示连接过程使用默认配置
        //例如集群名称为elasticsearch
        TransportClient client = getClient();

        //利用client方法，获取索引index01 tb_item id为83902的doc
        GetResponse response = client.prepareGet("index01","tb_item","83902").get();
        //从索引数据获取json字符串
        String get = response.getSourceAsString();
    }

    /**
     * 判断索引是否存在
     * @throws Exception
     */

    @Test
    public void test02() throws Exception{
        TransportClient client = getClient();
        //操作索引判断索引，需要层连接对象中去索引管理对象
        IndicesAdminClient indexClient = client.admin().indices();
        //索引管理对象调用exists方法，获取相应结果
        IndicesExistsResponse reponse01 = indexClient.prepareExists("index01").get();
        boolean result = reponse01.isExists();
    }

    /**
     * 创建索引
     * @throws Exception
     */
    public void test03() throws Exception{
        TransportClient client = getClient();
        //获取管理对象
        IndicesAdminClient client1 = client.admin().indices();
        CreateIndexResponse response = client1.prepareCreate("index03").get();
        System.out.println(response.isAcknowledged());//建好了为true
    }
    /**
     * 删除索引
     */
    public void test04() throws Exception{
        TransportClient client = getClient();
        IndicesAdminClient indexClient = client.admin().indices();
        indexClient.prepareDelete("index03").get();
    }


}
