package com.cdd.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import me.snowdrop.istio.api.model.IstioResource;
import me.snowdrop.istio.api.model.v1.rbac.ServiceRole;
import me.snowdrop.istio.client.IstioClient;
import me.snowdrop.istio.client.KubernetesAdapter;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import me.snowdrop.istio.api.model.v1.routing.*;

import static me.snowdrop.istio.api.internal.IstioSpecRegistry.getCRDNameFor;
import static me.snowdrop.istio.api.internal.IstioSpecRegistry.resolveIstioSpecForKind;

/**
 * KubernetesService
 *
 * @author liuruichao
 * Created on 2018/7/18 21:03
 */
public class KubernetesService {
    static Map<String,Object> objMap = new HashMap<>();

    static ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    static Pattern DOCUMENT_DELIMITER = Pattern.compile("---");

    static String KIND = "kind";

    public static void main(String[] args) throws IOException {
//        String yamlFile = "/Users/liuruichao/develop/istio/istio-1.0.0-snapshot.0/samples/bookinfo/kube/istio-rbac-enable.yaml";
        String yamlFile = "/Users/liuruichao/develop/istio/istio-1.0.0-snapshot.0/samples/bookinfo/kube/istio-rbac-productpage.yaml";
//        String yamlFile = "/Users/liuruichao/develop/istio/istio-1.0.0-snapshot.0/samples/bookinfo/routing/bookinfo-gateway.yaml";

        KubernetesClient client = new DefaultKubernetesClient();
        client = ((DefaultKubernetesClient) client).inNamespace("default");

        IstioClient istioClient = new IstioClient(new KubernetesAdapter(client));

        InputStream inputStream = new FileInputStream(yamlFile);
        String specFileAsString = readSpecFileFromInputStream(inputStream);
        System.out.println(specFileAsString);
        List<IstioResource> resources = convertIstioResource(specFileAsString);

        for (IstioResource resource : resources) {
//            istioClient.registerOrUpdateCustomResource(resource);
            istioClient.unregisterCustomResource(resource);
        }
//        istioClient.unregisterCustomResource(inputStream);

        /*KubernetesClient client = new DefaultKubernetesClient();

        InputStream in = new FileInputStream("/Users/liuruichao/develop/istio/istio-1.0.0-snapshot.0/samples/bookinfo/kube/istio-rbac-productpage.yaml");

        ((DefaultKubernetesClient) client).inNamespace("default").load(in).createOrReplace();

        in.close();*/

        /*CustomResourceDefinition crd = new CustomResourceDefinitionBuilder().
                withApiVersion("config.istio.io/v1alpha2").
                withNewMetadata().withName("productpage-viewer").withNamespace("default").endMetadata().
//                withNewSpec().withGroup(DUMMY_CRD_GROUP).withVersion("v1").withScope("Namespaced").
//                withNewNames().withKind("").withShortNames("dummy").withPlural("dummies").endNames().endSpec().
                build();
//        client.customResourceDefinitions().delete(crd);
        client.customResourceDefinitions().create(crd);
        System.out.println("Created CRD " + crd.getMetadata().getName());*/
    }

    public static List<IstioResource> convertIstioResource(final String specFileAsString) {
        List<IstioResource> results = new ArrayList<>();
        String[] documents = DOCUMENT_DELIMITER.split(specFileAsString);

        for (String document : documents) {
            try {
                document = document.trim();
                if (!document.isEmpty()) {
                    final Map<String, Object> resourceYaml = objectMapper.readValue(document, Map.class);

                    if (resourceYaml.containsKey(KIND)) {
                        final String kind = (String) resourceYaml.get(KIND);
                        getCRDNameFor(kind).orElseThrow(() -> new IllegalArgumentException(String.format("%s is not a known Istio resource.", kind)));
                        final IstioResource resource = objectMapper.convertValue(resourceYaml, IstioResource.class);
                        results.add(resource);
                    } else {
                        throw new IllegalArgumentException(String.format("%s is not specified in provided resource.", KIND));
                    }
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

        return results;
    }

    private static String readSpecFileFromInputStream(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toString();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read InputStream.", e);
        }
    }
}
